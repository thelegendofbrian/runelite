/*
 * Copyright (c) 2019, Brian <https://github.com/thelegendofbrian>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.chatspamgrabber;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.kit.KitType;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.slf4j.Logger;
import javax.inject.Inject;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

@PluginDescriptor(
	name = "Chat Spam Grabber",
	description = "Collects chat data to analyze",
	tags = {"messages"},
	enabledByDefault = false
)
@Slf4j
public class ChatSpamGrabberPlugin extends Plugin
{
	private static final ZoneId JAGEX = ZoneId.of("Europe/London");
	private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	@Inject
	private Client client;

	@Inject
	private ChatSpamGrabberConfig config;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Provides
	ChatSpamGrabberConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ChatSpamGrabberConfig.class);
	}

	@Override
	protected void startUp()
	{
		connect = connectToDatabase(config.databaseURL(), config.databasePort(), config.databaseName(), config.databaseUser(), config.databasePass());
	}

	protected Connection connectToDatabase(String databaseURL, int databasePort, String databaseName, String databaseUser, String databasePass)
	{
		Connection connect = null;

		try
		{
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager.getConnection("jdbc:mysql://" + databaseURL + ":" + databasePort + "/" + databaseName + "?user=" + databaseUser + "&password=" + databasePass);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return connect;
	}

	@Override
	public void shutDown()
	{
		try
		{
			connect.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("chatspamgrabber"))
		{
			connect = connectToDatabase(config.databaseURL(), config.databasePort(), config.databaseName(), config.databaseUser(), config.databasePass());
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		if (chatMessage.getType() != ChatMessageType.PUBLIC && (config.includePM() ? (chatMessage.getType() != ChatMessageType.PRIVATE_MESSAGE_RECEIVED) : chatMessage.getType() == ChatMessageType.PRIVATE_MESSAGE_RECEIVED) & chatMessage.getType() != ChatMessageType.AUTOCHAT)
		{
			return;
		}

		String message = chatMessage.getMessage();
		String name = chatMessage.getName();
		Logger logger = client.getLogger();

		List<Player> players = client.getPlayers();

		for (Player player : players)
		{
			String finalPlayerName = player.getName();

			Optional<Player> targetPlayer = players.stream()
					.filter(Objects::nonNull)
					.filter(p -> (p.getName() != null ? p.getName() : "").equals(finalPlayerName)).findFirst();
			if (targetPlayer.isPresent() && targetPlayer.get().getName() == name)
			{
				// Get the target player
				Player p = targetPlayer.get();

				processPlayerMySQL(p, finalPlayerName, chatMessage);
			}
		}
	}

	private void processPlayerMySQL(Player p, String playerName, ChatMessage chatMessage)
	{
		try
		{
			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("INSERT INTO chatspamgrabber.chats (`date`, `time`, `playerName`, `message`, `messageType`, `playerCombat`, `playerX`, `playerY`, `playerPlane`, `world`, `isMembersWorld`, `playerOrientation`, `cape`, `amulet`, `weapon`, `torso`, `shield`, `legs`, `head`, `hands`, `boots`, `jaw`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

			preparedStatement.setDate(1, getJagexDate());
			preparedStatement.setTime(2, getJagexTime());
			preparedStatement.setString(3, playerName);
			preparedStatement.setString(4, chatMessage.getMessage());
			preparedStatement.setString(5, chatMessage.getType().toString());
			preparedStatement.setInt(6, p.getCombatLevel());
			preparedStatement.setInt(7, p.getWorldLocation().getX());
			preparedStatement.setInt(8, p.getWorldLocation().getY());
			preparedStatement.setInt(9, p.getWorldLocation().getPlane());
			preparedStatement.setInt(10, client.getWorld());
			preparedStatement.setBoolean(11, client.getWorldType().contains(WorldType.MEMBERS));
			preparedStatement.setInt(12, p.getOrientation());
			preparedStatement.setInt(13, p.getPlayerComposition().getEquipmentId(KitType.CAPE));
			preparedStatement.setInt(14, p.getPlayerComposition().getEquipmentId(KitType.AMULET));
			preparedStatement.setInt(15, p.getPlayerComposition().getEquipmentId(KitType.WEAPON));
			preparedStatement.setInt(16, p.getPlayerComposition().getEquipmentId(KitType.TORSO));
			preparedStatement.setInt(17, p.getPlayerComposition().getEquipmentId(KitType.SHIELD));
			preparedStatement.setInt(18, p.getPlayerComposition().getEquipmentId(KitType.LEGS));
			preparedStatement.setInt(19, p.getPlayerComposition().getEquipmentId(KitType.HEAD));
			preparedStatement.setInt(20, p.getPlayerComposition().getEquipmentId(KitType.HANDS));
			preparedStatement.setInt(21, p.getPlayerComposition().getEquipmentId(KitType.BOOTS));
			preparedStatement.setInt(22, p.getPlayerComposition().getEquipmentId(KitType.JAW));

			preparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	private static Date getJagexDate()
	{
		Date date = Date.valueOf(LocalDate.now(JAGEX));

		return date;
	}

	private static Time getJagexTime()
	{
		Time time = Time.valueOf(LocalTime.now(JAGEX));

		return time;
	}
}

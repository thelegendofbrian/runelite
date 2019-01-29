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

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chatspamgrabber")
public interface ChatSpamGrabberConfig extends Config
{
	@ConfigItem(
		position = 1,
		keyName = "includePM",
		name = "Include PM's",
		description = ""
	)
	default boolean includePM()
	{
		return true;
	}

	@ConfigItem(
		position = 2,
		keyName = "databaseURL",
		name = "Database URL",
		description = ""
	)
	default String databaseURL()
	{
		return "localhost";
	}

	@ConfigItem(
			position = 3,
			keyName = "databaseName",
			name = "Database name",
			description = ""
	)
	default String databaseName()
	{
		return "chatspamgrabber";
	}

	@ConfigItem(
		position = 4,
		keyName = "databasePort",
		name = "Database port",
		description = ""
	)
	default int databasePort()
	{
		return 3306;
	}

	@ConfigItem(
		position = 5,
		keyName = "databaseUser",
		name = "Database user",
		description = ""
	)
	default String databaseUser()
	{
		return "chatspamgrabber";
	}

	@ConfigItem(
		position = 6,
		keyName = "databasePass",
		name = "Database password",
		description = ""
	)
	default String databasePass()
	{
		return "password";
	}

}

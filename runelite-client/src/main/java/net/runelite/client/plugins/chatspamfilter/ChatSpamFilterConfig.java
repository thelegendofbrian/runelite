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
package net.runelite.client.plugins.chatspamfilter;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chatspamfilter")
public interface ChatSpamFilterConfig extends Config
{
	@ConfigItem(
		position = 1,
		keyName = "hideSpamIn",
		name = "Hide public spam in",
		description = ""
	)
	default HideSpamIn hideSpamIn()
	{
		return HideSpamIn.CHATBOX;
	}

	@ConfigItem(
			position = 2,
			keyName = "scanPrivateChat",
			name = "Scan private chat for spam",
			description = ""
	)
	default boolean scanPrivateChat()
	{
		return true;
	}

	@ConfigItem(
			position = 3,
			keyName = "playerLevelMaximum",
			name = "Player level max to scan",
			description = ""
	)
	default int playerLevelMaximum()
	{
		return 60;
	}

	@ConfigItem(
			position = 4,
			keyName = "scanSpamEverywhere",
			name = "Scan for spam everywhere",
			description = ""
	)
	default boolean scanSpamEverywhere()
	{
		return false;
	}

	@ConfigItem(
			position = 5,
			keyName = "scanSpamGE",
			name = "Scan for spam at Grand Exchange",
			description = ""
	)
	default boolean scanSpamGE()
	{
		return true;
	}

	@ConfigItem(
			position = 6,
			keyName = "scanSpamLumbridge",
			name = "Scan for spam at Lumbridge castle",
			description = ""
	)
	default boolean scanSpamLumbridge()
	{
		return true;
	}

	@ConfigItem(
			position = 7,
			keyName = "scanSpamEdgeville",
			name = "Scan for spam at Edgeville bank",
			description = ""
	)
	default boolean scanSpamEdgeville()
	{
		return true;
	}

	@ConfigItem(
			position = 8,
			keyName = "scanSpamZMI",
			name = "Scan for spam at ZMI",
			description = ""
	)
	default boolean scanSpamZMI()
	{
		return true;
	}

	@ConfigItem(
			position = 9,
			keyName = "scanAutotext",
			name = "Scan autotext chat",
			description = ""
	)
	default boolean scanAutotext()
	{
		return true;
	}

	@ConfigItem(
			position = 10,
			keyName = "offerToReport",
			name = "Offer to report spammers",
			description = ""
	)
	default boolean offerToReport()
	{
		return false;
	}
}

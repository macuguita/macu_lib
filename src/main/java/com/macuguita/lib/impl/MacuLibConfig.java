/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.impl;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class MacuLibConfig extends WrappedConfig {

    @Comment("Config for the supporter perks")
    public Supporters supporters = new Supporters();

    public static class Supporters implements Section {
        @Comment("The amount of minutes to fetch changes in the role versions")
        @Comment("if the value is negative or 0 it only checks at startup")
        public int roleCheckerMinutesInterval = 60 * 12;
    }
}

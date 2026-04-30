-- All content related tables

-- region:      agents

create table public.valo_agent_roles (
    uuid uuid not null,
    display_name jsonb not null,    -- mapped lang codes to strings
    description jsonb not null,     -- mapped lang codes to strings
    display_icon text not null,

    constraint valo_agent_roles_pkey primary key (uuid)
);

create table public.valo_agents (
    uuid uuid not null,
    display_name jsonb not null,    -- mapped lang codes to strings
    description jsonb not null,     -- mapped lang codes to strings
    developer_name text not null,
    release_date timestamp with time zone not null,
    display_icon text not null,
    display_icon_small text not null,
    bust_portrait text not null,
    full_portrait text not null,
    full_portrait_v2 text not null,
    killfeed_portrait text not null,
    minimap_portrait text not null,
    home_screen_promo_title_image text,
    background text not null,
    background_gradient_colors text[] not null,
    is_full_portrait_right_facing boolean not null,
    is_base_content boolean not null,
    role uuid not null,

    constraint valo_agents_pkey primary key (uuid),
    constraint valo_agents_role_fkey foreign key (role) references valo_agent_roles(uuid) on update cascade on delete cascade
);

create table public.valo_agent_recruitments (
    agent uuid not null,
    xp integer not null,
    start_time timestamp with time zone not null,
    end_time timestamp with time zone not null,

    constraint valo_agent_recruitments_pkey primary key (agent),
    constraint valo_agent_recruitments_agent_fkey foreign key (agent) references valo_agents(uuid) on update cascade on delete cascade
);

create table public.valo_agent_abilities (
    agent uuid not null,
    slot text not null,
    display_name jsonb not null,    -- mapped lang codes to strings
    description jsonb not null,     -- mapped lang codes to strings
    display_icon text not null,

    constraint valo_agent_abilities_pkey primary key (agent, slot),
    constraint valo_agent_abilities_agent_fkey foreign key (agent) references valo_agents(uuid) on update cascade on delete cascade
);

-- endregion:   agents
-- region:      themes

create table public.valo_themes (
    uuid uuid not null,
    display_name jsonb not null,    -- mapped lang codes to strings
    display_icon text,
    store_featured_image text,

    constraint valo_themes_pkey primary key (uuid)
);

-- endregion:   themes
-- region:      content tiers

create table public.valo_content_tiers (
    uuid uuid not null,
    display_name jsonb not null,    -- mapped lang codes to strings
    developer_name text not null,
    display_icon text not null,
    juice_cost integer not null,
    juice_value integer not null,
    highlight_color text not null,
    rank integer not null,

    constraint valo_content_tiers_pkey primary key (uuid)
);

-- endregion    content tiers
-- region:      buddies

-- Table merged from /v1/buddies and /v1/buddies/levels
create table public.valo_buddies (
    uuid uuid not null,                 -- level uuid
    parent uuid not null,               -- buddy uuid
    theme uuid,                         -- buddy theme uuid
    display_name jsonb not null,        -- buddy name (mapped lang codes to strings)
    charm_level integer not null,       -- level number
    display_icon text not null,         -- level image
    hide_if_not_owned boolean not null, -- level flag

    constraint valo_buddies_pkey primary key (uuid),
    constraint valo_buddies_theme_fkey foreign key (theme) references valo_themes(uuid) on update cascade on delete cascade
);

-- endregion:   buddies
-- region:      ranks

create table public.valo_rank_tables (
    uuid uuid not null,

    constraint valo_rank_tables_pkey primary key (uuid)
);

create table public.valo_ranks (
    rank_table uuid not null,
    tier integer not null,
    tier_name jsonb not null,       -- mapped lang codes to strings
    small_icon text,
    large_icon text,
    rank_triangle_down_icon text,
    rank_triangle_up_icon text,
    color text not null,
    background_color text not null,
    division text not null,
    division_name jsonb not null,   -- mapped lang codes to strings

    constraint valo_ranks_pkey primary key (rank_table, tier),
    constraint valo_ranks_rank_table_fkey foreign key (rank_table) references valo_rank_tables(uuid) on update cascade on delete cascade
);

-- endregion:   ranks
-- region:      currencies

create table public.valo_currencies (
    uuid uuid not null,
    display_name jsonb not null,            -- mapped lang codes to strings
    display_name_singular jsonb not null,   -- mapped lang codes to strings
    display_icon text not null,
    large_icon text not null,
    reward_preview_icon text not null,

    constraint valo_currencies_pkey primary key (uuid)
);

-- endregion:   currencies
-- region:      events

create table public.valo_events (
    uuid uuid not null,
    display_name jsonb,         -- mapped lang codes to strings
    short_display_name jsonb,   -- mapped lang codes to strings
    start_time timestamp with time zone not null,
    end_time timestamp with time zone not null,

    constraint valo_events_pkey primary key (uuid)
);

-- endregion:   events
-- region:      flex

create table public.valo_flex (
    uuid uuid not null,
    display_name jsonb not null,            -- mapped lang codes to strings
    display_name_all_caps jsonb not null,   -- mapped lang codes to strings
    display_icon text not null,

    constraint valo_flex_pkey primary key (uuid)
);

-- endregion:   flex
-- region:      modes

create table public.valo_modes (
    uuid uuid not null,
    display_name jsonb not null,        -- mapped lang codes to strings
    description jsonb not null,         -- mapped lang codes to strings
    duration jsonb not null,            -- mapped lang codes to strings
    category text not null check (category in ('STANDARD', 'TUTORIAL', 'DEATHMATCH', 'TDM', 'SKIRMISH')),
    display_icon text,
    list_view_icon_tall text,
    rounds_per_half integer not null,

    constraint valo_modes_pkey primary key (uuid)
);

-- endregion:   modes
-- region:      maps

create table public.valo_maps (
    uuid uuid not null,
    display_name jsonb not null,    -- mapped lang codes to strings
    tactical_description jsonb,     -- mapped lang codes to strings
    coordinates jsonb,              -- mapped lang codes to strings
    category text not null check (category in ('STANDARD', 'TUTORIAL', 'TDM', 'SKIRMISH')),
    list_view_icon text not null,
    list_view_icon_tall text not null,
    splash text not null,
    premier_background_image text,
    stylized_background_image text,
    display_icon text,
    x_multiplier float not null,
    x_scalar_to_add float not null,
    y_multiplier float not null,
    y_scalar_to_add float not null,
    callouts jsonb,                 -- Raw JSON of all callouts

    constraint valo_maps_pkey primary key (uuid)
);

-- endregion:   maps
-- regon:       cards

create table public.valo_cards (
    uuid uuid not null,
    theme uuid,
    display_name jsonb not null,    -- mapped lang codes to strings
    display_icon text not null,
    large_art text,
    wide_art text not null,
    small_art text not null,
    hide_if_not_owned boolean not null,

    constraint valo_cards_pkey primary key (uuid),
    constraint valo_cards_theme_fkey foreign key (theme) references valo_themes(uuid) on update cascade on delete cascade
);

-- endregion:   cards
-- region:      titles

create table public.valo_titles (
    uuid uuid not null,
    display_name jsonb not null,    -- mapped lang codes to strings
    title_text jsonb not null,      -- mapped lang codes to strings
    hide_if_not_owned boolean not null,

    constraint valo_titles_pkey primary key (uuid)
);

-- endregion:   titles
-- region:      seasons

create table public.valo_seasons (
    uuid uuid not null,
    display_name jsonb not null,            -- mapped lang codes to strings
    episode_display_name jsonb not null,    -- mapped lang codes to strings
    title jsonb not null,                   -- mapped lang codes to strings (Constructed from episode and act display_names)
    start_time timestamp with time zone not null,
    end_time timestamp with time zone not null,

    constraint valo_seasons_pkey primary key (uuid)
);

create table public.valo_competitive_seasons (
    uuid uuid not null,
    season uuid not null,
    rank_table uuid not null,
    start_time timestamp with time zone not null,
    end_time timestamp with time zone not null,

    constraint valo_competitive_seasons_pkey primary key (uuid),
    constraint valo_competitive_seasons_season_fkey foreign key (season) references valo_seasons(uuid) on update cascade on delete cascade,
    constraint valo_competitive_seasons_rank_table_fkey foreign key (rank_table) references valo_rank_tables(uuid) on update cascade on delete cascade
);

create table public.valo_competitive_season_borders (
    uuid uuid not null,
    competitive_season uuid not null,
    display_icon text not null,
    small_icon text,
    level integer not null,
    wins_required integer not null,

    constraint valo_competitive_season_borders_pkey primary key (uuid),
    constraint valo_competitive_season_borders_competitive_season_fkey foreign key (competitive_season) references valo_competitive_seasons(uuid) on update cascade on delete cascade
);

-- endregion:   seasons
-- region:      sprays

create table public.valo_sprays (
    uuid uuid not null,
    theme uuid,
    display_name jsonb not null,    -- mapped lang codes to strings
    display_icon text not null,
    full_icon text,
    full_transparent_icon text,
    animation_png text,
    animation_gif text,
    hide_if_not_owned boolean not null,

    constraint valo_sprays_pkey primary key (uuid),
    constraint valo_sprays_theme_fkey foreign key (theme) references valo_themes(uuid) on update cascade on delete cascade
);

-- endregion:   sprays
-- region:      weapons and skins

create table public.valo_weapons (
    uuid uuid not null,
    display_name jsonb not null,    -- mapped lang codes to strings
    category text not null,
    default_skin uuid not null,
    display_icon text not null,
    kill_stream_icon text not null,

    constraint valo_weapons_pkey primary key (uuid)
);

create table public.valo_weapon_stats (
    weapon uuid not null,
    fire_rate float not null,
    magazine_size integer not null,
    run_speed_multiplier float not null,
    equip_time_seconds float not null,
    reload_time_seconds float not null,
    first_bullet_accuracy float not null,
    shotgun_pellet_count integer not null,
    wall_penetration text not null check (wall_penetration in ('LOW', 'HIGH', 'MEDIUM')),
    feature text check (feature in ('SILENCED', 'ROF_INCREASED', 'DUAL_ZOOM')),
    fire_mode text check (fire_mode in ('SEMI_AUTOMATIC')),
    alt_fire_type text check (alt_fire_type in ('ADS', 'AIR_BURST', 'SHOTGUN')),
    ads_stats jsonb,
    alt_shotgun_stats jsonb,
    air_burst_stats jsonb,
    damage_ranges jsonb not null,

    constraint valo_weapon_stats_pkey primary key (weapon),
    constraint valo_weapon_stats_weapon_fkey foreign key (weapon) references valo_weapons(uuid) on update cascade on delete cascade
);

create table public.valo_weapon_shop_data (
    weapon uuid not null,
    cost integer not null,
    category text not null,
    shop_order_priority integer not null,
    category_text jsonb not null,   -- mapped lang codes to strings
    grid_position jsonb,
    can_be_trashed boolean not null,
    image text not null,

    constraint valo_weapon_shop_data_pkey primary key (weapon),
    constraint valo_weapon_shop_data_weapon_fkey foreign key (weapon) references valo_weapons(uuid) on update cascade on delete cascade
);

create table public.valo_weapon_skins (
    uuid uuid not null,
    weapon uuid not null,
    display_name jsonb not null,    -- mapped lang codes to strings
    theme uuid not null,
    content_tier uuid,
    display_icon text,
    wallpaper text,

    constraint valo_weapon_skins_pkey primary key (uuid),
    constraint valo_weapon_skins_weapon_fkey foreign key (weapon) references valo_weapons(uuid) on update cascade on delete cascade,
    constraint valo_weapon_skins_theme_fkey foreign key (theme) references valo_themes(uuid) on update cascade on delete cascade,
    constraint valo_weapon_skins_content_tier_fkey foreign key (content_tier) references valo_content_tiers(uuid) on update cascade on delete cascade
);

create table public.valo_weapon_skin_chromas (
    uuid uuid not null,
    skin uuid not null,
    display_name jsonb not null,    -- mapped lang codes to strings
    display_icon text,
    full_render text not null,
    swatch text,
    streamed_video text,

    constraint valo_weapon_skin_chromas_pkey primary key (uuid),
    constraint valo_weapon_skin_chromas_skin_fkey foreign key (skin) references valo_weapon_skins(uuid) on update cascade on delete cascade
);

create table public.valo_weapon_skin_levels (
    uuid uuid not null,
    skin uuid not null,
    display_name jsonb not null,    -- mapped lang codes to strings
    display_icon text,
    streamed_video text,

    constraint valo_weapon_skin_levels_pkey primary key (uuid),
    constraint valo_weapon_skin_levels_skin_fkey foreign key (skin) references valo_weapon_skins(uuid) on update cascade on delete cascade
);

-- endregion:   weapons and skins
-- region:      progressions

create table public.valo_progressions (
    uuid uuid not null,
    display_name jsonb not null,    -- mapped lang codes to strings
    display_icon text,
    relation_type text not null check (relation_type in ('SEASON', 'EVENT', 'AGENT')),
    relation_uuid uuid not null,
    premium_vp_cost integer not null,

    constraint valo_progressions_pkey primary key (uuid)
);

create table public.valo_progression_levels (
    progression uuid not null,
    level_index integer not null,
    xp integer not null,
    is_purchasable_vp boolean not null,
    is_purchasable_kc boolean not null,
    vp_cost integer not null,
    kc_cost integer not null,
    is_epilogue boolean not null,

    constraint valo_progression_levels_pkey primary key (progression, level_index),
    constraint valo_progression_levels_progression_fkey foreign key (progression) references valo_progressions(uuid) on update cascade on delete cascade
);

create table public.valo_progression_level_rewards (
    progression uuid not null,
    level_index integer not null,
    sort_order integer not null,
    relation_type text not null check (relation_type in ('SPRAY', 'CURRENCY', 'SKIN', 'CARD', 'BUDDY', 'TITLE', 'FLEX')),
    relation_uuid uuid not null,
    amount integer not null,
    is_free boolean not null,

    constraint valo_progression_level_rewards_pkey primary key (progression, level_index, sort_order),
    constraint valo_progression_level_rewards_progress_level_index_fkey foreign key (progression, level_index) references valo_progression_levels(progression, level_index) on update cascade on delete cascade
);

-- endregion:   progressions
-- region:      version

create table public.valo_version (
    id integer default 0 check(id = 0),     -- ensure only one version can exist
    branch text not null,
    build_version text not null,
    manifest_id text not null,
    riot_client_build text not null,
    riot_client_version text not null,
    engine_version text not null,
    version text not null,
    build_date timestamp with time zone not null,

    constraint valo_version_pkey primary key (id)
);

-- endregion:   version
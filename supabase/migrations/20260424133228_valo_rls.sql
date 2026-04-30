-- RLS policies for the content tables

do $$
declare
    table_name text;
    tables text[] := array [
        'valo_agent_roles',
        'valo_agents',
        'valo_agent_recruitments',
        'valo_agent_abilities',
        'valo_themes',
        'valo_content_tiers',
        'valo_buddies',
        'valo_rank_tables',
        'valo_ranks',
        'valo_currencies',
        'valo_events',
        'valo_flex',
        'valo_modes',
        'valo_maps',
        'valo_cards',
        'valo_titles',
        'valo_seasons',
        'valo_competitive_seasons',
        'valo_competitive_season_borders',
        'valo_sprays',
        'valo_weapons',
        'valo_weapon_stats',
        'valo_weapon_shop_data',
        'valo_weapon_skins',
        'valo_weapon_skin_chromas',
        'valo_weapon_skin_levels',
        'valo_progressions',
        'valo_progression_levels',
        'valo_progression_level_rewards',
        'valo_version'
    ];
begin
    foreach table_name in array tables loop
        execute format('alter table public.%I enable row level security', table_name);

        execute format($policy$
            create policy "Users can read all rows from %1$s"
            on public.%1$I for select
            to authenticated
            using (true)
        $policy$, table_name);
    end loop;
end;
$$;
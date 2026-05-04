// This script generates SQL fixtures for running tests on the DB itself with "supabase test db"
// The content placed in the fixtures is a subset of actual content data

const API = "https://valorant-api.com/v1";

// Helper function to fetch API data and map to JSON
async function fetchJson(url: string): Promise<any[]> {
    const res = await fetch(url);
    const json = await res.json() as any;
    return json.data;
}

// Helper to escape single quotes for SQL
const sqlEscape = (value: string) => value.replace(/'/g, "''");

// Helper functions to map to SQL data types
const sqlString = (value: string | null | undefined) => value == null ? 'null' : `'${sqlEscape(value)}'`;
const sqlJsonb = (value: string | null | undefined) => value == null ? 'null' : `'${sqlEscape(JSON.stringify(value))}'::jsonb`;
const sqlUuid = (value: string | null | undefined) => value == null ? 'null' : `'${value}'::uuid`;
const sqlBoolean = (value: boolean) => value ? 'true' : 'false';
const sqlNumber = (value: number | null | undefined) => value == null ? 'null' : String(value);
const sqlArray = (values: string[]) => `array[${values.map(sqlString).join(', ')}]`
const sqlTimestampz = (value: string | null | undefined) => value == null ? 'null' : `'${value}'::timestamp with time zone`

// Helper function to construct insert statement
function insert(table: string, row: Record<string, string>): string {
    const columns = Object.keys(row).join(', ');
    const values = Object.values(row).join(', ');
    return `insert into public.${table} (${columns}) values (${values}) on conflict do nothing;`;
}

// Main function tht requests and processes data
async function main() {
    console.error('Generating content fixture');

    const lines: string[] = [];

    lines.push('-- Auto-generated fixture file. Do not edit manually.');
    lines.push('-- Regenerate on project root level with: deno run --allow-net scripts/generate-fixtures.ts > supabase/tests/fixtures/content.sql');
    lines.push('-- Generated at: ' + new Date().toISOString());
    lines.push('');

    // region:      -- agents

    lines.push('-- resion:\t\tagents');
    lines.push('');
    
    console.error('Fetching agents');
    const agents = await fetchJson(`${API}/agents?isPlayableCharacter=true&language=all`);
    const roles = [...new Map(agents
        .filter((agent: any) => agent.role != null)
        .map((agent: any) => [agent.role.uuid, agent.role] as [string, any])
    ).values()] as any[];

    console.error('Mapping agent roles');
    for (const role of roles) {
        lines.push(insert('valo_agent_roles', {
            uuid:                           sqlUuid(role.uuid),
            display_name:                   sqlJsonb(role.displayName),
            description:                    sqlJsonb(role.description),
            display_icon:                   sqlString(role.displayIcon)
        }));
    }

    lines.push('');

    console.error('Mapping agents');
    for(const agent of agents) {
        lines.push(insert('valo_agents', {
            uuid:                           sqlUuid(agent.uuid),
            display_name:                   sqlJsonb(agent.displayName),
            description:                    sqlJsonb(agent.description),
            developer_name:                 sqlString(agent.developerName),
            release_date:                   sqlTimestampz(agent.releaseDate),
            display_icon:                   sqlString(agent.displayIcon),
            display_icon_small:             sqlString(agent.displayIconSmall),
            bust_portrait:                  sqlString(agent.bustPortrait),
            full_portrait:                  sqlString(agent.fullPortrait),
            full_portrait_v2:               sqlString(agent.fullPortraitV2),
            killfeed_portrait:              sqlString(agent.killfeedPortrait),
            minimap_portrait:               sqlString(agent.minimapPortrait),
            home_screen_promo_title_image:  sqlString(agent.homeScreenPromoTileImage),
            background:                     sqlString(agent.background),
            background_gradient_colors:     sqlArray(agent.backgroundGradientColors),
            is_full_portrait_right_facing:  sqlBoolean(agent.isFullPortraitRightFacing),
            is_base_content:                sqlBoolean(agent.isBaseContent),
            role:                           sqlUuid(agent.role.uuid)
        }));

        for (const ability of agent.abilities ?? []) {
            if (ability.displayIcon == null) continue;
            lines.push(insert('valo_agent_abilities', {
                agent:                      sqlUuid(agent.uuid),
                slot:                       sqlString(ability.slot),
                display_name:               sqlJsonb(ability.displayName),
                description:                sqlJsonb(ability.description),
                display_icon:               sqlJsonb(ability.displayIcon)
            }));
        }

        if (agent.recruitmentData != null) {
            const recruitment = agent.recruitmentData;
            lines.push(insert('valo_agent_recruitments', {
                agent:                      sqlUuid(agent.uuid),
                xp:                         sqlNumber(recruitment.milestoneThreshold),
                start_time:                 sqlTimestampz(recruitment.startDate),
                end_time:                   sqlTimestampz(recruitment.endDate)
            }));
        }
    }

    lines.push('');
    lines.push('-- endregion:\tagents');

    // endregion:   -- agents
    // region:      -- themes

    lines.push('-- region:\t\tthemes');
    lines.push('');

    console.error('Fetching themes');
    const themes = await fetchJson(`${API}/themes?language=all`);

    console.error('Mapping themes');
    for (const theme of themes) {
        lines.push(insert('valo_themes', {
            uuid:                           sqlUuid(theme.uuid),
            display_name:                   sqlJsonb(theme.displayName),
            display_icon:                   sqlString(theme.displayIcon),
            store_featured_image:           sqlString(theme.storeFeaturedImage)
        }));
    }

    lines.push('');
    lines.push('-- endregion:\tthemes');

    // endregion:   -- themes
    // region:      -- content tiers

    lines.push('-- region:\t\content tiers');
    lines.push('');

    console.error('Fetching content tiers');
    const contentTiers = await fetchJson(`${API}/contenttiers?language=all`);

    console.error('Mapping content tiers');
    for (const contentTier of contentTiers) {
        lines.push(insert('valo_content_tiers', {
            uuid:                           sqlUuid(contentTier.uuid),
            display_name:                   sqlJsonb(contentTier.displayName),
            developer_name:                 sqlString(contentTier.developerName),
            display_icon:                   sqlString(contentTier.displayIcon),
            juice_cost:                     sqlNumber(contentTier.juiceCost),
            juice_value:                    sqlNumber(contentTier.juiceValue),
            highlight_color:                sqlString(contentTier.highlightColor),
            rank:                           sqlNumber(contentTier.rank)
        }));                      
    }
    
    lines.push('');
    lines.push('-- endregion:\tcontent tiers');

    // endregion:   -- content tiers
    // region:      -- buddies

    lines.push('-- region:\t\tbuddies');
    lines.push('');

    console.error('Fetching buddies');
    const buddies = await fetchJson(`${API}/buddies?language=all`);

    console.error('Mapping buddies');
    for (const buddy of buddies) {
        const buddyLevel = buddy.levels[0];
        lines.push(insert('valo_buddies', {
            uuid:                           sqlUuid(buddyLevel.uuid),
            parent:                         sqlUuid(buddy.uuid),
            theme:                          sqlUuid(buddy.theme),
            display_name:                   sqlJsonb(buddy.displayName),
            charm_level:                    sqlNumber(buddyLevel.charmLevel),
            display_icon:                   sqlString(buddyLevel.displayIcon),
            hide_if_not_owned:              sqlBoolean(buddyLevel.hideIfNotOwned)
        }));
    }
    
    lines.push('');
    lines.push('-- endregion:\tbuddies');

    // endregion:   -- buddies

    console.error('Done, printing')
    console.log(lines.join('\n'));
}

main().catch(console.error)
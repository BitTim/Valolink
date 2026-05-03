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

    // region:      -- Agents

    lines.push('-- resion:\t\tAgents');
    
    console.error('Fetching agents');
    const agents = await fetchJson(`${API}/agents?isPlayableCharacter=true&language=all`);
    const roles = [...new Map(agents
        .filter((agent: any) => agent.role != null)
        .map((agent: any) => [agent.role.uuid, agent.role] as [string, any])
    ).values()] as any[];

    console.error('Mapping roles');
    for (const role of roles) {
        lines.push(insert('valo_agent_roles', {
            uuid:                           sqlUuid(role.uuid),
            display_name:                   sqlJsonb(role.displayName),
            description:                    sqlJsonb(role.description),
            display_icon:                   sqlString(role.displayIcon)
        }));
    }

    lines.push('');

    console.error('Mapping Agents');
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
    }

    lines.push('-- endregion:\tAgents');

    // endregion:   -- Agents

    console.error('Done, printing')
    console.log(lines.join('\n'));
}

main().catch(console.error)
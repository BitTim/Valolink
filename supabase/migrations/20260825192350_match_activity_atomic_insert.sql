create type activity_input as (
    time timestamptz,
    type text,
    xp integer,
    rr integer,
    mode uuid
);

create type match_input as (
    score_a integer,
    score_b integer,
    end_reason text,
    is_ranked boolean,
    time timestamptz,
    map uuid,
    mode uuid
);

create type participant_input as (
    visible_rr integer,
    is_owner boolean,
    is_team_b boolean
);

create or replace function insert_match_activity(
    p_activity activity_input,
    p_match match_input,
    p_participant participant_input
)
returns uuid
language plpgsql
security invoker
as $$
declare
    v_activity_id uuid;
    v_match_id uuid;
begin
    insert into activities (user_id, time, type, xp, rr, mode)
    values (auth.uid(), p_activity.time, p_activity.type, p_activity.xp, p_activity.rr, p_activity.mode)
    returning id into v_activity_id;

    insert into matches (score_a, score_b, end_reason, is_ranked, time, map, mode)
    values (p_match.score_a, p_match.score_b, p_match.end_reason, p_match.is_ranked, p_match.time, p_match.map, p_match.mode)
    returning id into v_match_id;

    insert into match_participants (user_id, activity, match, visible_rr, is_owner, is_team_b)
    values (auth.uid(), v_activity_id, v_match_id, p_participant.visible_rr, p_participant.is_owner, p_participant.is_team_b);

    return v_activity_id;
end;
$$;
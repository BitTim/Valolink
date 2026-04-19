create table public.users (
    id uuid not null default auth.uid(),
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    username text not null default '',
    avatar text,
    is_private boolean not null default false,

    constraint users_pkey primary key (id),
    constraint users_id_fkey foreign key (id) references auth.users(id) on update cascade on delete cascade
);

create table public.flags (
    user_id uuid not null default auth.uid(),
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    has_onboarded boolean not null default false,

    constraint flags_pkey primary key (user_id),
    constraint flags_user_id_fkey foreign key (user_id) references users(id) on update cascade on delete cascade
);

create table public.follows (
    follower uuid not null default auth.uid(),
    following uuid not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    relation_status text not null default 'PENDING' check (relation_status in ('PENDING', 'ACCEPTED', 'BLOCKED')),

    constraint follows_pkey primary key (follower, following),
    constraint follows_no_self check (follower != following),
    constraint follows_follower_fkey foreign key (follower) references users(id) on update cascade on delete cascade,
    constraint follows_following_fkey foreign key (following) references users(id) on update cascade on delete cascade
);

create table public.agents (
    user_id uuid not null default auth.uid(),
    agent uuid not null,
    created_at timestamp with time zone not null default now(),

    constraint agents_pkey primary key (user_id, agent),
    constraint agents_user_id_fkey foreign key (user_id) references users(id) on update cascade on delete cascade
);

create table public.progressions (
    user_id uuid not null default auth.uid(),
    progression uuid not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    free_only boolean not null default false,
    xp_offset integer not null default 0,
    total_xp integer not null default 0 check (total_xp >= 0),

    constraint progressions_pkey primary key (user_id, progression),
    constraint progressions_user_id_fkey foreign key (user_id) references users(id) on update cascade on delete cascade
);

create table public.purchased_levels (
    user_id uuid not null default auth.uid(),
    progression uuid not null,
    level uuid not null,
    created_at timestamp with time zone not null default now(),

    constraint purchased_levels_pkey primary key (user_id, progression, level),
    constraint purchased_levels_user_id_progression_fkey foreign key (user_id, progression) references progressions(user_id, progression) on update cascade on delete cascade
);

create table public.activities (
    id uuid not null default gen_random_uuid(),
    user_id uuid not null default auth.uid(),
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    type text not null check (type in ('MATCH', 'PLACEMENT', 'RR_REFUND', 'XP_CORRECTION')),
    xp integer not null default 0,
    rr integer default null,

    constraint activities_pkey primary key (user_id, id),
    constraint activities_user_id_fkey foreign key (user_id) references users(id) on update cascade on delete cascade
);

create table public.matches (
    id uuid not null default gen_random_uuid(),
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    score_a integer not null default 0 check (score_a >= 0),
    score_b integer check (score_b >= 0),
    end_reason text not null default 'COMPLETED' check (end_reason in ('COMPLETED', 'SURRENDER_A', 'SURRENDER_B')),
    time timestamp with time zone not null default now(),
    map uuid not null,
    mode uuid not null,

    constraint matches_pkey primary key (id)
);

create table public.match_participants (
    user_id uuid not null default auth.uid(),
    activity uuid not null,
    match uuid not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    is_owner boolean not null default false,
    is_team_b boolean not null default false,

    constraint match_participants_pkey primary key (user_id, activity),
    constraint match_participants_activity_fkey foreign key (user_id, activity) references activities(user_id, id) on update cascade on delete cascade,
    constraint match_participants_match_fkey foreign key (match) references matches(id) on update cascade on delete cascade deferrable
);

create unique index match_participants_one_owner_per_match
on match_participants (match)
where is_owner = true;
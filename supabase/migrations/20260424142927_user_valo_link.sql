-- Link user space tables to the new valo content tables

alter table agents add constraint agents_agent_fkey foreign key (agent) references valo_agents(uuid) on update cascade on delete cascade;
alter table progressions add constraint progressions_progression_fkey foreign key (progression) references valo_progressions(uuid) on update cascade on delete cascade;
alter table purchased_levels add constraint purchased_levels_progression_level_index_fkey foreign key (progression, level_index) references valo_progression_levels(progression, level_index) on update cascade on delete cascade;
alter table matches add constraint matches_map_fkey foreign key (map) references valo_maps(uuid) on update cascade on delete cascade;
alter table matches add constraint matches_mode_fkey foreign key (mode) references valo_modes(uuid) on update cascade on delete cascade;
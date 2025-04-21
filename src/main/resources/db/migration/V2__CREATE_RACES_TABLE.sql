create table races (
    id UUID primary key,
    distance double precision not null,
    duration integer not null,
    start_time timestamptz not null,
    end_time timestamptz not null,
    user_id UUID not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    constraint fk_races_user_id foreign key (user_id) references users(id) on delete cascade
);
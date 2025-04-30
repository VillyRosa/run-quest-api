create table password_reset_tokens (
    id UUID primary key,
    user_id UUID not null,
    code_hash varchar(255) not null,
    used_at timestamptz,
    attempts integer not null default 0,
    expires_at timestamptz not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    constraint fk_password_reset_user_id foreign key (user_id) references users(id) on delete cascade
);
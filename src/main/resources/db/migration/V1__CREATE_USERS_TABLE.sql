create table users (
    id UUID primary key,
    username varchar(255) not null,
    email varchar(255) unique not null,
    email_verified_at timestamptz,
    password varchar(255) not null,
    status varchar(50) not null default 'ACTIVE',
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);
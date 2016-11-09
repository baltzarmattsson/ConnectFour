create database connectfour;
use connectfour;

create table Game(
id integer not null auto_increment,
datePlayed datetime default current_timestamp,
nbrofcols int,
nbrofrows int,
nbrofplayers int,
constraint PK_ID primary key (id)
);

create table Credentials(
username varchar(60) not null,
password varchar(60) not null,
constraint PK_USERNAME primary key (username)
);

create table Player(
username varchar(60) not null,
name varchar(150),
playerid varchar(25) default 'Player',
constraint PK_USERNAME_PLAYER primary key (username),
constraint FK_USERNAME_PLAYER foreign key (username) references Credentials (username) on delete cascade
);

create table Gamestatus(
gameid integer not null,
finished boolean,
winner varchar(60) default null,
date datetime default current_timestamp,
constraint PK_GAMEID_GAMESTATUS primary key (gameid),
constraint FK_GAMEID_GAMESTATUS foreign key (gameid) references Game (id) on delete cascade,
constraint FK_WINNER_GAMESTATUS foreign key (winner) references Credentials (username) on delete set null
);

create table Playing(
playernbr int not null,
gameid integer not null,
color varchar(60),
username varchar(60),
isAI boolean,
aiDifficulty varchar(15),
constraint PK_PLAYING_PLAYERNBR primary key (playernbr, gameid),
constraint FK_PLAYING_PLAYERNBR foreign key (username) references Credentials (username) on delete cascade,
constraint FK_PLAYING_GAMEID foreign key (gameid) references Game (id) on delete cascade
);

create table Played(
username varchar(60) not null,
gameid integer not null,
constraint PK_PLAYED_USERRNAME primary key (username, gameid),
constraint FK_PLAYED_USERNAME foreign key (username) references Credentials (username) on delete cascade,
constraint FK_PLAYED_GAMEID foreign key (gameid) references Game (id) on delete cascade
);

create table Moves(
gameid integer not null,
movenbr integer not null,
player integer,
username varchar(60) default null,
rowCoord integer not null,
colCoord integer not null,
color varchar(15),
date datetime default current_timestamp,
constraint PK_MOVES_GAMEID_MOVENBR primary key (gameid, movenbr),
constraint FK_MOVES_GAMEID foreign key (gameid) references Game (id) on delete cascade,
constraint FK_MOVES_PLAYER foreign key (username) references Credentials (username) on delete set null
);
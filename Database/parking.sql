DROP TABLE IF EXISTS Acccounts, Logs, Spaces, Prices, Reservations;
CREATE TABLE Accounts (
	UserID VARCHAR(256) NOT NULL,
	Salt VARCHAR(256) NOT NULL,
	Hash VARCHAR(256) NOT NULL,
	Permit VARCHAR(16) NOT NULL,
	AccountType VARCHAR(16)
	PRIMARY KEY(UserID),
	FOREIGN KEY(Permit) REFERENCES Spaces(Permit),
);
CREATE TABLE Logs (
	UserID VARCHAR(256) NOT NULL,
	Lot VARCHAR(256) NOT NULL,
	Space INT NOT NULL,
	TimeIn DateTime NOT NULL,
	TimeOut DateTime,
	Rate FLOAT,
	PRIMARY KEY(UserID, TimeIn),
	UNIQUE(Lot, Space, TimeIn),
	FOREIGN KEY(UserID) REFERENCES Accounts(UserID),
	FOREIGN KEY(Lot, Space) REFERENCES Spaces(Lot, Space),
	FOREIGN KEY(Rate) REFERENCES Prices(RatePerHour)
);
CREATE TABLE Spaces (
	Lot VARCHAR(256) NOT NULL,
	Space INT NOT NULL,
	Permit VARCHAR(16) NOT NULL,
	PriceRateClass INT NOT NULL,
	PRIMARY KEY(Lot, Space),
	FOREIGN KEY(PriceRateClass) REFERENCES Prices(PriceRateClass)
);
CREATE TABLE Prices(
	PriceRateClass INT NOT NULL,
	StartTime Time NOT NULL,
	EndTime Time NOT NULL,
	RatePerHour Float NOT NULL,
	ReservationPrice Float NOT NULL,
	PRIMARY KEY(PriceRateClass, StartTime, EndTime)
);
CREATE TABLE Reservations(
	UserID VARCHAR(256) NOT NULL,
	Lot VARCHAR(256) NOT NULL,
	Space INT NOT NULL,
	ScheduleStart DateTime NOT NULL,
	ScheduleEnd DateTime NOT NULL,
	Completion DateTime,
	ReservationPrice Float,
	PRIMARY KEY(UserID, Lot, Space, ScheduleStart),
	FOREIGN KEY(Lot, Space) REFERENCES Spaces(Lot, Space),
	FOREIGN KEY(UserID) REFERENCES Accounts(UserID),
	FOREIGN KEY(ReservationPrice) REFERENCES Prices(ReservationPrice)
)

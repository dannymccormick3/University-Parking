const express = require('express');
const app = express();
const mysql = require('mysql');
const moment = require('moment');
const connection = mysql.createConnection({
	host:'parkingdatabase.c9dcrnrodp1p.us-east-2.rds.amazonaws.com',
 	user: 'ParkTeam',
 	password:'variablerateplanning',
 	database:'parking'
});
const LIMIT = 100;
connection.connect((err)=> {
})
app.get('/makeReservation', (req, res) => {
	var toIns = {}
	toIns.UserID = req.query.UserID;
	toIns.Lot = req.query.Lot;
	toIns.Space = req.query.Space;
	if(req.query.ScheduleStart) {
		toIns.ScheduleStart = req.query.ScheduleStart;
		toIns.ScheduleEnd = req.query.ScheduleEnd;
	} else {
		toIns.ScheduleStart = moment().format('YYYY-MM-DD HH:MM:SS');
		toIns.ScheduleEnd = moment().add(30, 'minutes').format('YYYY-MM-DD HH:MM:SS');
	}
	toIns.Completion = null;
	var startquery = "INSERT INTO Reservations SET ?";
	connection.query(startquery, toIns, (err, sqlres) => {
		if(err){ throw err;}
		else{
			var obj = {};
			obj["ScheduleEnd"] = sqlres.ScheduleEnd;
			obj["Lot"] = sqlres.Lot;
			obj["Space"] = sqlres.Space;
			res.send(JSON.stringify(obj));
		}

	})

});

app.get('/getAvailableLots', (req, res) => {
	var startquery = "SELECT Lot, Count(Space) as Numspaces "
										+ "FROM Spaces "
										+ "WHERE Occupied = 0 AND Permit = \" " + req.query.permit + "\" "
										+ " GROUP BY Lot";
	console.log(startquery);
	connection.query(startquery, (err, rows) => {
		if(err) {
			console.log(err);
		}
		var obj = {};
		var count =0;
		rows.forEach( (row) => {
			var temp = {};
			temp.Lot = row.Lot;
			temp.Spaces = row.Numspaces;
			obj[count.toString()]= temp;
			count++;
		});
		res.send(JSON.stringify(obj));
	})
});

app.get('/getReservations', (req, res) => {
	var startquery = "SELECT * "
										+ "FROM Reservations "
										+ "WHERE UserID = " + req.query.UserID +
										" LIMIT " + LIMIT;
	console.log(startquery);
	connection.query(startquery, (err, rows) => {
		if(err) {
			console.log(err);
		}
		var obj = {};
		var count =0;
		rows.forEach( (row) => {
		  //	UserID VARCHAR(256) NOT NULL,
			// 	Lot VARCHAR(256) NOT NULL,
			// 	Space INT NOT NULL,
			// 	ScheduleStart DateTime NOT NULL,
			// 	ScheduleEnd DateTime NOT NULL,
			// 	Completion DateTime,
			// 	ReservationPrice Float,
			var temp = {};
			temp.Lot = row.Lot;
			temp.Space = row.Space;
			temp.ScheduleStart = row.ScheduleStart;
			temp.ScheduleEnd = row.ScheduleEnd;
			temp.Completion = row.Completion;
			temp.ReservationPrice = row.ReservationPrice;
			obj[count.toString()]= temp;
			count++;
		});
		res.send(JSON.stringify(obj));
	})
});


app.get('/getPermit', (req, res) => {
	var startquery = "SELECT Permit "
										+ "FROM Accounts "
										+ "WHERE UserID = " + req.query.UserID;
	console.log(startquery);
	connection.query(startquery, (err, rows) => {
		if(err) {
			console.log(err);
		}
		var obj = {};
		rows.forEach( (row) => {
			obj['permit']= row.Permit;
		});
		res.send(JSON.stringify(obj));
	})
});

app.get('/getAvailableSpots', (req, res) => {
	var Date = new Date();
	var startquery = "SELECT Space, RatePerHour, ReservationPrice "
										+ "FROM Spaces x, Prices y "
										+ "WHERE x.Occupied = 0 AND x.Permit = \" " + req.query.permit + "\" "
										+ " AND x.PriceRateClass = y.PriceRateClass AND  GROUP BY Lot";
	console.log(startquery);
	connection.query(startquery, (err, rows) => {
		if(err) {
			console.log(err);
		}
		var obj = {};
		var count =0;
		rows.forEach( (row) => {
			var temp = {};
			temp.Lot = row.Lot;
			temp.Spaces = row.Numspaces;
			obj[count.toString()]= temp;
			count++;
		});
		res.send(JSON.stringify(obj));
	})
});


app.listen(3000);

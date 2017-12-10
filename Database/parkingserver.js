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
	let toIns = {}
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
	let startquery = "INSERT INTO Reservations SET ?";
	connection.query(startquery, toIns, (err, sqlres) => {
		if(err){ 
			let resObj = {"Failure": "True"};
			res.send(JSON.stringify(resObj));
			console.log(err);}
		else{
			let obj = {};
			obj["ScheduleEnd"] = sqlres.ScheduleEnd;
			obj["Lot"] = sqlres.Lot;
			obj["Space"] = sqlres.Space;
			res.send(JSON.stringify(obj));
		}

	})

});

app.get('/getAvailableLots', (req, res) => {
	let startquery = "SELECT Lot, Count(Space) as Numspaces "
										+ "FROM Spaces "
										+ "WHERE Occupied = 0 AND Permit = \" " + req.query.permit + "\" "
										+ " GROUP BY Lot";
	console.log(startquery);
	connection.query(startquery, (err, rows) => {
		if(err) {
			let resObj = {"Failure": "True"};
			res.send(JSON.stringify(resObj));
			console.log(err);
		}
		let obj = {};
		let count =0;
		rows.forEach( (row) => {
			let temp = {};
			temp.Lot = row.Lot;
			temp.Spaces = row.Numspaces;
			obj[count.toString()]= temp;
			count++;
		});
		res.send(JSON.stringify(obj));
	})
});

app.get('/getReservations', (req, res) => {
	let startquery = "SELECT * "
										+ "FROM Reservations "
										+ "WHERE UserID = " + req.query.UserID +
										" LIMIT " + LIMIT;
	console.log(startquery);
	connection.query(startquery, (err, rows) => {
		if(err) {
			let resObj = {"Failure": "True"};
			res.send(JSON.stringify(resObj));
			console.log(err);
		}
		let obj = {};
		let count =0;
		rows.forEach( (row) => {
		  //	UserID VARCHAR(256) NOT NULL,
			// 	Lot VARCHAR(256) NOT NULL,
			// 	Space INT NOT NULL,
			// 	ScheduleStart DateTime NOT NULL,
			// 	ScheduleEnd DateTime NOT NULL,
			// 	Completion DateTime,
			// 	ReservationPrice Float,
			let temp = {};
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
	let startquery = "SELECT Permit "
										+ "FROM Accounts "
										+ "WHERE UserID = " + req.query.UserID;
	console.log(startquery);
	connection.query(startquery, (err, rows) => {
		if(err) {
			let resObj = {"Failure": "True"};
			res.send(JSON.stringify(resObj));
			console.log(err);
		}
		let obj = {};
		rows.forEach( (row) => {
			obj['permit']= row.Permit;
		});
		res.send(JSON.stringify(obj));
	})
});

app.get('/setPermit', (req, res) => {
	let startquery = "UPDATE Accounts "
										+ "Set Permit =  " + req.query.Permit
										+ " WHERE UserID = " + req.query.UserID;
	console.log(startquery);
	connection.query(startquery, (err, result) => {
		if(err) {
			let resObj = {"Failure": "True"};
			res.send(JSON.stringify(resObj));
			console.log(err);
		}
		let obj = {};
		obj['UserID'] = result.UserID;
		obj['Permit'] = result.Permit;
		res.send(JSON.stringify(obj));
	})
});

app.get('/getAvailableSpots', (req, res) => {
	let Date = new Date();
	let startquery = "SELECT Space, RatePerHour, ReservationPrice "
										+ "FROM Spaces x, Prices y "
										+ "WHERE x.Occupied = 0 AND x.Permit = \" " + req.query.permit + "\" "
										+ " AND x.PriceRateClass = y.PriceRateClass AND  GROUP BY Lot";
	console.log(startquery);
	connection.query(startquery, (err, rows) => {
		if(err) {
			console.log(err);
		}
		let obj = {};
		let count =0;
		rows.forEach( (row) => {
			let temp = {};
			temp.Lot = row.Lot;
			temp.Spaces = row.Numspaces;
			obj[count.toString()]= temp;
			count++;
		});
		res.send(JSON.stringify(obj));
	})
});

app.get('/checkIn', (req, res) => {
	let startquery = "SELECT * "
		+ "FROM Reservations "
		+ "WHERE UserID = " + req.query.UserID +
		" AND Lot = " + req.query.Lot + 
		" AND Space = " + req.query.Space;
	console.log(startquery);
	connection.query(startquery, (err, rows) => {
	if(err) {
		let resObj = {"Failure": "True"};
		res.send(JSON.stringify(resObj));
		console.log(err);
	}
	let obj = {};
	let count = 0;
	rows.forEach( (row) => {
		count++;
	});
	if(count ==0) {
		let toIns = {}
		toIns["UserID"] = req.query.UserID;
		toIns["Lot"] = req.query.Lot;
		toIns["Space"] = req.query.Space;
		toIns["TimeIn"] = moment().format('YYYY-MM-DD HH:MM:SS');
		let curquery = "INSERT INTO Logs SET ?";
		connection.query(curquery, toIns, (err, sqlres)=> {
			if(err){ 
				let resObj = {"Failure": "True"};
				res.send(JSON.stringify(resObj));
				console.log(err);
			} else {
				//#TODO update occupied in logs, maybe have that be before the reservation check to make it more easily undoable
				let resObj = {"Success":"Parked in available spot"};
				res.send(JSON.stringify(resObj));
			}
		})	
	} else if (count ==1) {

	}
	res.send(JSON.stringify(obj));
	})
	
});


app.listen(3000);

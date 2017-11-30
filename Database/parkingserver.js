const express = require('express');
const app = express();
const mysql = require('mysql');
const connection = mysql.createConnection({
	host:'parkingdatabase.c9dcrnrodp1p.us-east-2.rds.amazonaws.com',
 	user: 'ParkTeam',
 	password:'variablerateplanning',
 	database:'parking'
});
connection.connect((err)=> {

})
app.get('/makeReservation', (req, res) => {
	var startquery = ""

})

app.get('/getAvailableLots', (req, res) => {
	var startquery = "SELECT Lot, Count(Space) as Numspaces"
										+ "FROM Spaces"
										+ "WHERE Occupied = 0 AND Permit = " + req.query.permit
										+ "GROUP BY Lot";
	connection.query(startquery, (err, rows) => {
		if(err) {
			throw err;
		}
		var obj = {};
		var count =0;
		rows.forEach( (row) => {
			temp = {};
			temp.Lot = row.Lot;
			temp.Spaces = row.Numspaces;
			obj[count.toString()]= temp;
			count++;
		});
		res.send(JSON.stringify(temp));
	})
})

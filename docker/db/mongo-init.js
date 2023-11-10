db = db.getSiblingDB("events");
 
db.createCollection("event");

db = db.getSiblingDB("admin");

db.createUser({
	user: "event",
	pwd: process.env.MONGO_INITDB_PASSWORD,
	roles: [{ role: "readWrite", db: "events" }],
});



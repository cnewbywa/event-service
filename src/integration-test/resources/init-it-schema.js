db = db.getSiblingDB("events");

db.event.insertOne( {
    eventId: "922b2539-696f-4de9-a2e2-eb373c0bf8a0",
    affectedId: "5211fc21-b6b5-44db-b528-71be8a81afda",
    message: "Event message 1",
    application: "test",
    action: "ADD",
    time: ISODate("2023-10-29T10:00:00.000Z")
} );

db.event.insertOne( {
    eventId: "02efa2d2-6e26-465e-be94-61f9ffda1f27",
    affectedId: "c507c57c-4e8e-4aa8-97ed-db5900f9ea1b",
    message: "Event message 2",
    application: "test",
    action: "MODIFY",
    time: ISODate("2023-11-04T09:09:09.000Z")
} );

db.event.insertOne( {
    eventId: "ce432365-0642-4f52-a205-b93b47276345",
    affectedId: "bde45c80-b476-4992-a191-776cf262252a",
    message: "Event message 3",
    application: "test 2",
    action: "ADD",
    time: ISODate("2023-11-13T10:00:00.000Z")
} );
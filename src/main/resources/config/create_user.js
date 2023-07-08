db.createCollection("users", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["id", "login", "firstName", "lastName", "imageUrl", "email", "password", "createdDate", "lastModifiedDate"],
            properties: {
                id: { bsonType: "string" },
                login: { bsonType: "string" },
                firstName: { bsonType: "string" },
                lastName: { bsonType: "string" },
                imageUrl: { bsonType: "string" },
                email: { bsonType: "string" },
                password: { bsonType: "string" },
                createdDate: { bsonType: "date" },
                lastModifiedDate: { bsonType: "date" }
            }
        }
    }
});

// MongoDB connection parameters
const dbUser = 'root';
const dbPassword = 'root';
const dbName = 'api';

// Connect to the admin database to create the user
db = db.getSiblingDB('api');

// Create the user with root privileges
db.createUser({
    user: dbUser,
    pwd: dbPassword,
    roles: [
        { role: 'root', db: 'admin' }
    ]
});

print(`User '${dbUser}' created with password '${dbPassword}'`);

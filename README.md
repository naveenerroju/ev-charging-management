# EV Charger Management System

This project is an **EV Charger Management System** that allows you to manage charging stations, process transactions, and interact with chargers through WebSocket and REST APIs.

## Features

- **WebSocket Communication**:
    - **Boot Notification**: Charger sends boot notification to the system.
    - **Heartbeat**: Charger sends periodic heartbeat to the system.
    - **Status Notification**: Charger sends its status to the system.
    - **Start Transaction**: Charger starts a transaction.
    - **Stop Transaction**: Charger stops a transaction.

- **REST API Endpoints**:
    - **Get Transactions within a Time Range**: Retrieve transactions within a specified time range.
    - **Get All Charging Stations**: Retrieve the list of all charging stations with their statuses.

## Prerequisites

- **Java 17 or higher**
- **Spring Boot** (version 3.4.4)
- **MongoDB** (for storing charging station data and transaction records)
- **Maven** (for building the project)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/naveenerroju/ev-charging-management.git
cd ev-charging-management
```

### 2. Set up MongoDB

Make sure you have MongoDB installed and running. By default, MongoDB runs on `localhost:27017`.

If you don't have MongoDB set up, you can use a **Docker container** to run it:

```bash
docker run -d -p 27017:27017 --name mongodb mongo
```

### 3. Configure MongoDB Connection

Update your `application.properties` file in the `src/main/resources` folder to configure the connection to MongoDB:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/ev_charger_db
```

### 4. Build and Run the Application

You can build and run the application using **Maven**:

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on **localhost** at port **8080**.

### 5. WebSocket Endpoint

The WebSocket server listens for incoming connections at:

```
ws://localhost:8080/ws/station/{stationId}
```

Clients can send WebSocket messages to interact with the system. Below are the payloads for various actions:

#### **1. BootNotification** (Sent by Charger)

**Request Payload:**
```json
{
  "action": "BootNotification",
  "messageId": "1",
  "payload": {
    "chargePointVendor": "EVCorp",
    "chargePointModel": "AlphaEVC",
    "status": "Available"
  }
}
```

**Response Payload:**
```json
{
  "action": "BootNotification",
  "messageId": "1",
  "payload": {
    "status": "Accepted",
    "currentTime": "2025-03-28T19:22:41"
  }
}
```

#### **2. Heartbeat** (Sent by Charger)

**Request Payload:**
```json
{
  "action": "Heartbeat",
  "messageId": "2",
  "payload": {}
}
```

**Response Payload:**
```json
{
  "action": "Heartbeat",
  "messageId": "2",
  "payload": {
    "status": "Accepted",
    "currentTime": "2025-03-28T19:23:13"
  }
}
```

#### **3. StartTransaction** (Sent by Charger)

**Request Payload:**
```json
{
  "action": "StartTransaction",
  "messageId": "123",
  "payload": {
    "status": "Charging",
    "timestamp": "2025-03-27T22:07:09",
    "chargerId": "STS_001",
    "energyConsumed": 0
  }
}
```

**Response Payload:**
```json
{
  "action": "StartTransaction",
  "messageId": "123",
  "payload": {
    "status": "Accepted",
    "currentTime": "2025-03-28T19:24:00",
    "transactionId": "6c4eb1b0-2f83-4ee3-8db9-a744d9e9a53a"
  }
}
```

#### **4. StopTransaction** (Sent by Charger)

**Request Payload:**
```json
{
  "action": "StopTransaction",
  "messageId": "1234",
  "payload": {
    "transactionId": "3ef22a69-49ca-49e7-aab7-b67c5bb2d49c",
    "status": "Finishing",
    "timestamp": "2025-03-27T22:07:09",
    "chargerId": "STS_001",
    "energyConsumed": 20,
    "cost": 20.5
  }
}
```

**Response Payload:**
```json
{
  "action": "StopTransaction",
  "messageId": "1234",
  "payload": {
    "status": "Accepted",
    "currentTime": "2025-03-28T19:24:35",
    "transactionId": "3ef22a69-49ca-49e7-aab7-b67c5bb2d49c"
  }
}
```

---

### **REST API Endpoints**

#### **1. Get Transactions within a Time Range**

Fetches all transactions that occurred between the given **startTime** and **endTime**.

**GET Request:**
```http
GET http://localhost:8080/internal/transaction?startTime=2025-03-01T00:00:00&endTime=2025-03-02T00:00:00
```

**Response:**
```json
[
    {
        "status": "Completed",
        "currentTime": null,
        "transactionId": "3ef22a69-49ca-49e7-aab7-b67c5bb2d49c"
    },
    {
        "status": "Processing",
        "currentTime": null,
        "transactionId": "6c4eb1b0-2f83-4ee3-8db9-a744d9e9a53a"
    }
]
```

#### **2. Get All Charging Stations**

Fetches all charging stations with their status.

**GET Request:**
```http
GET http://localhost:8080/internal/stations
```

**Response:**
```json
[
    {
        "id": "STS_001",
        "status": "Finishing"
    },
    {
        "id": "STS_002",
        "status": "Available"
    }
]
```

---

### **Testing the System**

You can test the WebSocket and REST APIs using the following tools:

1. **Postman**: For testing the REST API endpoints (such as `/internal/transaction` and `/internal/stations`).
2. **WebSocket Client**: Use tools like **Postman** (WebSocket support) or **any WebSocket client** (e.g., **wscat**) to test the WebSocket connection and send messages like **BootNotification**, **Heartbeat**, **StartTransaction**, and **StopTransaction**.

#### Example WebSocket Client Command using `wscat`:
```bash
wscat -c ws://localhost:8080/ws/charger
```

Then send the **BootNotification** message:

```json
{
  "action": "BootNotification",
  "messageId": "1",
  "payload": {
    "chargePointVendor": "EVCorp",
    "chargePointModel": "AlphaEVC",
    "status": "Available"
  }
}
```

### **Troubleshooting**

- Ensure that MongoDB is running and connected.
- Check the application logs for any errors related to WebSocket connections or MongoDB interactions.
- Use Postman or a WebSocket client to verify if the messages are being sent and received correctly.

---

### **Conclusion**

This documentation provides an overview of the **EV Charger Management System**, detailing the WebSocket and REST API interactions. You can use this system to manage charger status, track transactions, and monitor the health of the charging stations.

Let me know if you need further details or modifications to this documentation!
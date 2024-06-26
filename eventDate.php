<?php

// Database connection detail

$host = "127.0.0.1";
$username = "root";
$password = "";
$dbname = "calendar_data";

//create connection

$conn = new mysqli($host, $username, $password, $dbname);

// Check connection
if ($conn -> connect_error) {
    die("connection failed: " . $conn -> connect_error);
}

// Get data request
if (isset($_POST['eventName']) && isset ($_POST['eventDes']) 
&& isset ($_POST['eventDate'])) {



//Sanitize input 
if (empty($data['eventName'])) {
    die("Error: Event name cannot be empty.");
}

$eventName = $conn -> real_escape_string ($_POST['eventName']);
$eventDes = $conn -> real_escape_string ($_POST['eventDes']);

}

//Format data
$eventDate = date('Y-m-d H:i:s', $_POST['eventDate'] );

$sql = "INSERT INTO calendar_events (eventName, eventDes, eventDate) 
VALUES ('$eventName', '$eventDes','$eventDate')";

if ($conn -> query($sql) === TRUE) {
    echo "Event added successfully";
} else {
    echo "Error add event. Please try again " . $sql . "<br>" . $conn -> error;
    error_log("Database error: " . $conn->error . " - Query: " . $sql); 
}

$conn -> close();
?>


<?php
$host = "127.0.0.1";
$username = "root";
$password = "";   // default password for "root" user is empty
$dbname =  "map_system";


// Connect to server
$connect=mysqli_connect($host, $username, $password, $dbname) 
                    or die ("Sorry, unable to connect database server");

//$dbselect=mysql_select_db($dbname,$connect) 
//                    or die ("Sorry, unable to connect database");

// Run the query
$result = mysqli_query($connect,"SELECT * FROM map");

if ($result) {

//   while ($row = mysql_fetch_array($result)) {
//      $id         = $row["id"];
//      $name = $row["name"];
//      $gps = $row["gps"];
//      $Price      = $row["Price"];
//      $picture    = $row["picture"];
//      $googleUrl      = $row["googleUrl "];
//      $information = $row["information"];
//
//      print "$name $gps ($Price)\n";
//      print "at $picture\n";
//      print "$googleUrl\n";
//      print "$information\n";
//      print "#";
//   }



    while ($row = mysqli_fetch_assoc($result)) {
	$output[] = $row;
    }

    print("{\"map\":");
    print(json_encode($output));
    print("}");
}

mysqli_close($connect);
?>


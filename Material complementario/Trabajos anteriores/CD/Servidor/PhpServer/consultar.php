<?php


	$conexion = mysql_connect("mysql.hostinger.es","u328937656_base1","proyectosi1");
if (!$conexion)
  {
  die('No se ha podido conectar con la base de datos: ' . mysql_error());
  }

mysql_select_db("u328937656_base1", $conexion);

  if(!isset($GET["username"])){
	$username=$_GET["username"]; 
  }
	
  if(!isset($GET["password"])){
	$password=$_GET["password"]; 
  }

$result=mysql_query("SELECT * FROM users where username = '".$username."'
                     AND password = '".$password."' ");

if(mysql_num_rows($result)>0){
	$row = mysql_fetch_assoc($result);
 	$lastLogin = time();
	$lastIP = $_SERVER['REMOTE_ADDR'];
	$cierto = 1;
	$sentencia = mysql_query(" UPDATE users SET lastLoginTime= '".$lastLogin."', lastLoginIP = '".$lastIP."', login = '".$cierto."' WHERE username = '".$username."'AND password = '".$password."' ");
    echo "SI";
}else{
	echo "NO";
}

/*echo json_encode($data);*/
mysql_close($conexion);
?>
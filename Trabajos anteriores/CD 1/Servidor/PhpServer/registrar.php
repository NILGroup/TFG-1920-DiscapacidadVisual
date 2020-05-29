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
  
  if(!isset($GET["nombre"])){
  	$nombre = $_GET["nombre"];
  }
  
  if(!isset($GET["apellidos"])){
  	$apellidos = $_GET["apellidos"];
  }
	$query="SELECT username FROM users WHERE username='".$username."';";
	$result=mysql_query($query);
	$falso = 0;
	if(mysql_num_rows($result)>0){
		echo "Usuario ya existente";
	}else{
		$query = "INSERT INTO users VALUES ('".$username."','".$password."','".$nombre."','". $apellidos ."', null, null, '".$falso."', '".$falso."');";
  		$result=mysql_query($query);
  		if($result==true){
			echo "Registro completado";
		}else{
			echo "Error en el registro";
		}
	}
  
mysql_close($conexion);

?>
<?php
	$conexion = mysql_connect("mysql.hostinger.es","u328937656_base1","proyectosi1");
	if (!$conexion){
		die('No se ha podido conectar con la base de datos: ' . mysql_error());
	}
	mysql_select_db("u328937656_base1", $conexion);
	$query = "SELECT * FROM test.repository;";
	$result = mysql_query($query);
	echo "[";
	if(mysql_num_rows($result)>0){
		$i = 0;
		while($row = mysql_fetch_array($result)){
			if($i!=0){
				echo ",";
			}
			$archivo = array();
			$archivo[0] = $row["MAC"];
			$archivo[1] = $row["x"];
			$archivo[2] = $row["y"];
			$archivo[3] = $row["z"];
			$archivo[4] = $row["strength"];
			echo json_encode($archivo);
			$i = $i + 1;
		}
	}
	echo "]";
	mysql_close($conexion);
	
?>
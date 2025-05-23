for $x in //vehiculo
where $x/modelo = "Golf"
return concat($x/propietario/nombre/text(), " ", $x/propietario/apellidos)
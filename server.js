const mysql = require('mysql');
const express = require('express'),
  app = express(),
  port = process.env.PORT || 7000;
const session = require('express-session');
const path = require('path');
const bodyparser = require('body-parser');
const moment = require('moment');
const multer = require('multer');
const fileType = require('file-type')
const fs = require('fs')
app.use(session({
	secret: 'secret',
	resave: true,
	saveUninitialized: true
}));

app.use(bodyparser.json());


var mysqlConnection = mysql.createConnection({
    host: '127.0.0.1', //route
    user: 'root', //
    password: '123',
    database: 'meusac',
    multipleStatements: true
});

mysqlConnection.connect((err) => {
    if (!err)
        console.log('DB connection succeded.');
    else
        console.log('DB connection failed \n Error : ' + JSON.stringify(err, undefined, 2));
});


app.listen(port);

console.log('todo list RESTful API server started on: ' + port);


//Get all students
app.get('/estudiante', (req, res) => {
    mysqlConnection.query('SELECT * FROM estudiante', (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});

//Get single student
app.get('/estudiante/:id', (req, res) => {
    mysqlConnection.query('SELECT * FROM estudiante WHERE carnet = ?', [req.params.id], (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});

//Delete an student
app.delete('/estudiante/:id', (req, res) => {
    mysqlConnection.query('DELETE FROM estudiante WHERE carnet = ?', [req.params.id], (err, rows, fields) => {
        if (!err)
            res.send('Deleted successfully.');
        else
            console.log(err);
    })
});


//Insert or update an student
app.post('/estudiante', (req, res) => {
    let emp = req.body;
    var sql = "SET @carnet = ?;SET @nombre = ?;SET @apellido = ?;SET @telefono = ?;SET @direccion = ?; SET @fechaNacimiento = ?;SET @contrasenia = ?;SET @numeroCarrera = ?;\
    CALL estudianteAddOrEdit(@carnet,@nombre,@apellido,@telefono,@direccion,@fechaNacimiento,@contrasenia, @numeroCarrera);";
    mysqlConnection.query(sql, [emp.carnet, emp.nombre, emp.apellido, emp.telefono,emp.direccion, emp.fechaNacimiento, emp.contrasenia, emp.numeroCarrera], (err, rows, fields) => {
        if (!err)
            res.send('Updated successfully');

        else
			res.send('Updated insuccessfully');
            console.log(err);
    })
});

app.put('/estudiante', (req, res) => {
    let emp = req.body;
    var sql = "SET @carnet = ?;SET @nombre = ?;SET @apellido = ?;SET @telefono = ?;SET @direccion = ?; SET @fechaNacimiento = ?;SET @contrasenia = ?;SET @numeroCarrera = ?;\
    CALL estudianteAddOrEdit(@carnet,@nombre,@apellido,@telefono,@direccion,@fechaNacimiento,@contrasenia, @numeroCarrera);";
    mysqlConnection.query(sql, [emp.carnet, emp.nombre, emp.apellido, emp.telefono,emp.direccion, emp.fechaNacimiento, emp.contrasenia, emp.numeroCarrera], (err, rows, fields) => {
        if (!err)
            res.send('Updated successfully');

        else
            console.log(err);
    })
});








//Get all catedratico
app.get('/catedratico', (req, res) => {
    mysqlConnection.query('SELECT * FROM catedratico', (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});

//Get single student
app.get('/catedratico/:id', (req, res) => {
    mysqlConnection.query('SELECT * FROM catedratico WHERE usuario = ?', [req.params.id], (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});

//Delete an student
app.delete('/catedratico/:id', (req, res) => {
    mysqlConnection.query('DELETE FROM catedratico WHERE dpi = ?', [req.params.id], (err, rows, fields) => {
        if (!err)
            res.send('Deleted successfully.');
        else
            console.log(err);
    })
});

//Insert or update an student
app.post('/catedratico', (req, res) => {
    let emp = req.body;
    var sql = "SET @dpi = ?;SET @nombre = ?;SET @apellido = ?;SET @telefono = ?;SET @direccion = ?; SET @fechaNacimiento = ?;SET @contrasenia = ?; SET @usuario = ?;\
    CALL catedraticoAddOrEdit(@dpi,@nombre,@apellido,@telefono,@direccion,@fechaNacimiento,@contrasenia, @usuario);";
    mysqlConnection.query(sql, [emp.dpi, emp.nombre, emp.apellido, emp.telefono,emp.direccion, emp.fechaNacimiento, emp.contrasenia, emp.usuario], (err, rows, fields) => {
        if (!err)
            res.send('Updated successfully');

        else
            console.log(err);
    })
});

app.put('/catedratico', (req, res) => {
    let emp = req.body;
    var sql = "SET @dpi = ?;SET @nombre = ?;SET @apellido = ?;SET @telefono = ?;SET @direccion = ?; SET @fechaNacimiento = ?;SET @contrasenia = ?;\
    CALL catedraticoAddOrEdit(@dpi,@nombre,@apellido,@telefono,@direccion,@fechaNacimiento,@contrasenia, @numeroCarrera);";
    mysqlConnection.query(sql, [emp.dpi, emp.nombre, emp.apellido, emp.telefono,emp.direccion, emp.fechaNacimiento, emp.contrasenia], (err, rows, fields) => {
        if (!err)
            res.send('Updated successfully');

        else
            console.log(err);
    })
});




//Get all assistance
app.get('/asistencia', (req, res) => {
    mysqlConnection.query('SELECT * FROM asistencia', (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});

//Get single assistance from carnet
app.get('/asistencia/:id', (req, res) => {
    mysqlConnection.query('SELECT * FROM asistencia WHERE carnet = ?', [req.params.id], (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});



//Insert or update an assistance
app.post('/asistencia', (req, res) => {
	let emp = req.body;
    var sql = "INSERT INTO asistencia(fecha, carnet)\
		VALUES (CURRENT_TIMESTAMP(), ?);";	    
		mysqlConnection.query(sql, [emp.carnet], (err, rows, fields) => {
        if (!err)
            res.send('Updated successfully');

        else
            console.log(err);
    })
});








//Get all cursos
app.get('/curso', (req, res) => {
    mysqlConnection.query('call showallcursos();', (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});

//Get single curso
app.get('/curso/:id', (req, res) => {
    mysqlConnection.query('SELECT * FROM curso WHERE id = ?', [req.params.id], (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});

//Delete an student
app.delete('/curso/:id', (req, res) => {
    mysqlConnection.query('DELETE FROM curso WHERE id = ?', [req.params.id], (err, rows, fields) => {
        if (!err)
            res.send('Deleted successfully.');
        else
            console.log(err);
    })
});

//Insert or update an student
app.post('/curso', (req, res) => {
    let emp = req.body;
    var sql = "INSERT INTO curso(nombreCurso)\
		VALUES (?);";
    mysqlConnection.query(sql, [emp.nombreCurso], (err, rows, fields) => {
        if (!err)
            res.send('Updated successfully');

        else
            console.log(err);
    })
});

app.put('/curso', (req, res) => {
    let emp = req.body;
    var sql = "INSERT INTO curso(nombreCurso)\
		VALUES (?);";
    mysqlConnection.query(sql, [emp.nombreCurso], (err, rows, fields) => {
        if (!err)
            res.send('Updated successfully');

        else
            console.log(err);
    })
});




//Get all actividades
app.get('/actividad', (req, res) => {
    mysqlConnection.query('SELECT * FROM actividad', (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});

//Get single curso
app.get('/actividad/:id', (req, res) => {
    mysqlConnection.query('SELECT * FROM actividad WHERE id = ?', [req.params.id], (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});

//Delete an student
app.delete('/actividad/:id', (req, res) => {
    mysqlConnection.query('DELETE FROM actividad WHERE id = ?', [req.params.id], (err, rows, fields) => {
        if (!err)
            res.send('Deleted successfully.');
        else
            console.log(err);
    })
});

//Insert or update an student
app.post('/actividad', (req, res) => {
    let emp = req.body;
    var sql = "INSERT INTO actividad(nombreActividad)\
		VALUES (?);";
    mysqlConnection.query(sql, [emp.nombreActividad], (err, rows, fields) => {
        if (!err)
            res.send('Updated successfully');

        else
            console.log(err);
    })
});

app.put('/actividad', (req, res) => {
    let emp = req.body;
    var sql = "INSERT INTO actividad(nombreActividad)\
		VALUES (?);";
    mysqlConnection.query(sql, [emp.nombreActividad], (err, rows, fields) => {
        if (!err)
            res.send('Updated successfully');

        else
            console.log(err);
    })
});








//Get all detalle de actividad
app.get('/detalleCursoActividad', (req, res) => {
    mysqlConnection.query('SELECT * FROM detalleCursoActividad', (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});

//Get single detalle actividad by curso
app.get('/detalleCursoActividad/curso/:id', (req, res) => {
    mysqlConnection.query('SELECT * FROM detalleCursoActividad WHERE idcurso = ?', [req.params.id], (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});


//Get single detalle actividad by actividad
app.get('/detalleCursoActividad/actividad/:id', (req, res) => {
    mysqlConnection.query('SELECT * FROM detalleCursoActividad WHERE idactividad = ?', [req.params.id], (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});







//Get all detalle de inscripcion
app.get('/detallece', (req, res) => {
    mysqlConnection.query('SELECT * FROM detallecce', (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});

//Get single detalle actividad by curso
app.get('/detallece/curso/:id', (req, res) => {
    mysqlConnection.query('SELECT * FROM detallece WHERE idcurso = ?', [req.params.id], (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});


//Get single detalle actividad by catedratico
app.get('/detallece/catedratico/:id', (req, res) => {
    mysqlConnection.query('SELECT * FROM detallece WHERE dpi = ?', [req.params.id], (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});


//Get single detalle actividad by estudiante
app.get('/detallece/estudiante/:id', (req, res) => {
    mysqlConnection.query('SELECT * FROM meusac.detallecce, meusac.curso \
WHERE meusac.detallecce.idcurso = meusac.curso.id \
AND meusac.detallecce.carnet = ?;', [req.params.id], (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});

//Insert or update an student
app.post('/detallece', (req, res) => {
    let emp = req.body;
    var sql = "INSERT INTO detallece(idcurso, dpi, carnet)\
		VALUES (?, ? , ?);";
    mysqlConnection.query(sql, [emp.idcurso, emp.dpi, emp.carnet], (err, rows, fields) => {
        if (!err)
            res.send('Updated successfully');

        else
            console.log(err);
    })
});






app.post('/auth', function(request, response) {
	var username = request.body.username;
	var password = request.body.password;
	if (username && password) {
		mysqlConnection.query('SELECT * FROM estudiante WHERE carnet = ? AND contrasenia = ?', [username, password], function(error, results, fields) {
			if (results.length > 0) {
				request.session.loggedin = true;
				request.session.username = username;
				response.redirect('/home');
			} else {
				response.send('Incorrect Username and/or Password!');
			}			
			response.end();
		});
	} else {
		response.send('Please enter Username and Password!');
		response.end();
	}
});

//Get all detalle de actividad
app.get('/ActividadCurso', (req, res) => {
    mysqlConnection.query('SELECT * FROM meusac.detalleCursoActividad, meusac.actividad\
	WHERE meusac.detalleCursoActividad.idactividad  = meusac.actividad.id', (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});

app.get('/home', function(request, response) {
	if (request.session.loggedin) {
		response.send('Welcome back, ' + request.session.username + '!');
	} else {
		response.send('Please login to view this page!');
	}
	response.end();
});

app.get('/logout', function (req, res) {
  delete req.session.user_id;
  res.redirect('/login');
}); 
/*
const upload = multer({
    dest:'images/', 
    limits: {fileSize: 10000000, files: 1},
    fileFilter:  (req, file, callback) => {
    
        if (!file.originalname.match(/\.(jpg|jpeg)$/)) {

            return callback(new Error('Only Images are allowed !'), false)
        }

        callback(null, true);
    }
}).single('image')

app.post('/images/upload', (req, res) => {

    upload(req, res, function (err) {

        if (err) {

           http://192.168.43.72:3306res.status(400).json({message: err.message})

        } else {

            let path = `/images/${req.file.filename}`
            res.status(200).json({message: 'Image Uploaded Successfully !', path: path})
        }
    })
})

app.get('/images/:imagename', (req, res) => {

    let imagename = req.params.imagename
    let imagepath = __dirname + "/images/" + imagename
    let image = fs.readFileSync(imagepath)
    let mime = fileType(image).mime

	res.writeHead(200, {'Content-Type': mime })
	res.end(image, 'binary')
})*/
app.post('/detalleCursoActividad', (req, res) => {
    let emp = req.body;
    var sql = "INSERT INTO detalleCursoActividad(nota, idcurso, idactividad, nombreActividad)\
		VALUES (?, ? , ?, ?);";
    mysqlConnection.query(sql, [emp.nota, emp.idcurso, emp.idactividad, emp.nombreActividad], (err, rows, fields) => {
        if (!err)
            res.send('Updated successfully');

        else
            console.log(err);
    })
});
//agregado para la descarga de archivos

var upload = require('express-fileupload');
app.use(upload()); 
app.get('/',function(req,res){
    res.sendFile(__dirname+'/index.html');
  })
  app.post('/upload',function(req,res){
    console.log(req.body.curso);
    let id = req.body.curso;
    if(req.files.upfile){
      var file = req.files.upfile,
        name = id+"_"+file.name,
        type = file.mimetype;
      var uploadpath = __dirname + '/uploads/' + name;
      file.mv(uploadpath,function(err){
        if(err){
          console.log("File Upload Failed",name,err);
          res.send("Error Occured!")
        }
        else {
          console.log("File Uploaded",name);
          res.send('Done! Uploading files')
          var sql = "INSERT INTO Archivo(nombre, id)\
		VALUES (?, (SELECT  id FROM curso WHERE nombreCurso = ? LIMIT 1 ));";
        mysqlConnection.query(sql, [name, id], (err, rows, fields) => {
        
    })
        }
      });
    }
    else {
      res.send("No File selected !");
      res.end();
    };
  })
  app.post('/upload2',function(req,res){
    console.log(req.body.id);
    let id = req.body.id;
    if(req.files.upfile){
      var file = req.files.upfile,
        name = id+"_"+file.name,
        type = file.mimetype;
      var uploadpath = __dirname + '/uploads/' + id+".jpg";
      file.mv(uploadpath,function(err){
        if(err){
          console.log("File Upload Failed",name,err);
          res.send("Error Occured!")
        }
        else {
          console.log("File Uploaded",name);
          res.send('Done! Uploading files')
         
        }
      });
    }
    else {
      res.send("No File selected !");
      res.end();
    };
  })
      
//Insert or update an student

  app.get('/upload', (req, res) => {
   // console.log(req.params.imagenameename);
    let imagename = req.body.imagename
    let imagepath = __dirname + "/uploads/" + imagename
    let image = fs.readFileSync(imagepath)
    let mime = fileType(image).mime

	res.writeHead(200, {'Content-Type': mime })
	res.end(image, 'binary')
})
app.get('/download', function(req, res){
    var file = __dirname + '/uploads/Clase2.pptx';
    res.download(file); // Set disposition and send it.
  });
  app.get('/Curso2', (req, res) => {
    var sql = "Select nombreCurso from curso";
    mysqlConnection.query(sql,(err, rows, fields) => {
        if (!err)
        res.send(rows);
        else
            console.log(err);
    })
});
 //get all uploads files
app.get('/archivos', (req, res) => {
    var sql = "SELECT * FROM Archivo";
    mysqlConnection.query(sql,(err, rows, fields) => {
        if (!err)
        res.send(rows);
        else
            console.log(err);
    })
});
app.get('/Alumnos', (req, res) => {
    var sql = "SELECT * FROM estudiante";
    mysqlConnection.query(sql,(err, rows, fields) => {
        if (!err)
        res.send(rows);
        else
            console.log(err);
    })
});
app.get('/Alumnos/:id', (req, res) => {
    mysqlConnection.query('SELECT * FROM estudiante WHERE carnet = ?', [req.params.id], (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});
 app.get('/download/:nombre', function(req, res){
    var nombre=req.params.nombre;
    var file = __dirname + '/uploads/'+nombre;
    res.download(file); // Set disposition and send it.
});
var express = require('express');
var mongodb = require('mongodb');
var passwordHash = require('password-hash');
var bodyParser = require('body-parser');

var app = express();
var port = process.env.port || 3000;

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

var MongoClient = mongodb.MongoClient;

var url = 'mongodb://localhost:27017';

MongoClient.connect(url, { useNewUrlParser: true }, function (err, client) {
    if (err)
        console.log("MongoDb bağlantı sırasında bir hata oluştu.")
    else {
        var db = client.db('mydb');
        var userRef = db.collection('user');
        app.post('/register', (request, response) => {
            var data = request.body;

            var name = data.name;
            var email = data.email;
            var password = data.password;

            var hashedPassword = passwordHash.generate(password);

            var insertJson = {
                'name': name,
                'email': email,
                'password': hashedPassword,
            }

            userRef.find({ 'email': email }).count(function (err, number) {
                if (number > 0)
                    response.json({ 'success': false, 'message': "Bu mail adresi daha önce alınmış !" });
                else {
                    userRef.insertOne(insertJson, function (err, res) {
                        if (err)
                            response.json({ 'success': false, 'message': "Kayıt işlemi sırasında bir hata meydana geldi" });
                        else
                            response.json({ 'success': true, 'id': insertJson._id });
                    })
                }
            })

        })

        app.post('/login', (request, response) => {
            var data = request.body;

            var email = data.email;
            var password = data.password;


            userRef.findOne({ 'email': email }, function (err, user) {
                if (!user)
                    response.json({ 'success': false, 'message': "Mail adresi bulunamadı !" });
                else {
                    var hashedPassword = user.password;

                    if (passwordHash.verify(password, hashedPassword)) {
                        var userJson = {
                            'success': true,
                            'id': user._id,
                            'name': user.name,
                        }
                        response.json(userJson);
                    }
                    else
                        response.json({ 'success': false, 'message': "Şifrenizi yanlış girdiniz !" })

                }
            })

        })
    }
})

app.listen(3000, () => {
    console.log('Server %d portunda çalışmaya başladı', port)
})

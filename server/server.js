var express = require('express');
var app = express();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var _ = require('lodash');
var fs = require('fs')
var util = require('util')

app.set('port', (process.env.PORT || 3000));

server.listen(app.get('port'), function() {
    console.log('NBBNETS Chat Server is running on port ', app.get('port'));
});

var DataSource = require('./ds');
var data = DataSource();

function getData(){
    let dataNil = _.chain(data).filter(a => {
        return a.recipients != "" && a.recipients != null && a.completed == false;
    }).sortBy(function (obj) {
        return parseInt(obj.id);
    });


    return {
        data : _.sortBy(data, function (obj) {
            return parseInt(obj.id);
        }),
        last : _.chain(data).filter({completed : true}).sortBy(['id','desc']).last().value(),
        nextInLine : dataNil,
    };
}

io.on('connection', function(socket) {

    socket.emit('queue', getData());
    console.log("new connection");
    
    socket.on('register', ({id,recipients,photo}) => {
        data.map(a => {
            if(a.id == id){
                a.recipients = recipients;
                a.photo = photo;
            }
        })
        // fs.writeFileSync('./backup.js',  util.inspect(data) , 'utf-8');

        fs.writeFileSync('./backup.js',  JSON.stringify(data) , 'utf-8');
        io.sockets.emit('queue',getData());

    });

    socket.on('next',str =>{
        let last  = _.chain(data).filter({completed : true}).sortBy(['id','desc']).first().value();
        let award = _.chain(data).filter(a => {
            return a.recipients != "" && a.recipients != null && a.completed == false;
        }).sortBy(['id','desc']).first().value();
        award.completed = true;
        fs.writeFileSync('./backup.js',  JSON.stringify(award) , 'utf-8');
        io.sockets.emit('queue',getData());
    });

    socket.on('update',({id,award}) => {
        if(id){
            let o = _.find(data,{id:id});
            o.agency = award.agency;
            o.award = award.award;
            o.seats = award.seats;
            o.tableAssignment = award.tableAssignment;
            o.recipients = award.recipients;
            o.completed = award.completed;
            io.sockets.emit('queue',getData());
            fs.writeFileSync('./backup.js',  JSON.stringify(data) , 'utf-8');
        }
    })

    socket.on('backup', function(){
        fs.writeFileSync('./backup.js',  JSON.stringify(data) , 'utf-8');
    });

});
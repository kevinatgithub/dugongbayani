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
    return {
        data : _.sortBy(data,['id'],['desc']),
        last : _.chain(data).filter({completed : true}).sortBy(['id','desc']).last().value(),
        nextInLine : _.chain(data).filter(a => {
            return a.recipients != "" && a.recipients != null && a.completed == false;
        }).sortBy(['id'],['desc']).take(10),
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
        io.sockets.emit('queue',getData());

    });

    socket.on('next',str =>{
        let last  = _.chain(data).filter({completed : true}).sortBy(['id','desc']).first().value();
        let award = _.chain(data).filter(a => {
            return a.recipients != "" && a.recipients != null && a.completed == false;
        }).sortBy(['id','desc']).first().value();
        award.completed = true;
        io.sockets.emit('queue',getData());
    });

});
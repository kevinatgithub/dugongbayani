/**
 * First we will load all of this project's JavaScript dependencies which
 * includes Vue and other libraries. It is a great starting point when
 * building robust, powerful web applications using Vue and Laravel.
 */

String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.replace(new RegExp(search, 'g'), replacement);
};

import VueSession from 'vue-session';
import VeeValidate from 'vee-validate';
import VueGoogleCharts from 'vue-google-charts'
import _ from 'lodash';
import ioClient from 'socket.io-client';



import router from './router';
import store from './store';
import http from './http';
import error from './components/Tools/error.vue';



import _globals from './_globals';

require('./bootstrap');

window.Vue = require('vue');
import './filters';

Vue.use(VueSession);
Vue.use(VeeValidate);
Vue.use(VueGoogleCharts);
Vue.prototype.$http = http;

Vue.component('start', require('./components/Start.vue'));
Vue.component('error',error);

/**
 * Next, we will create a fresh Vue application instance and attach it to
 * the page. Then, you may begin adding components to this application
 * or customize the JavaScript scaffolding to fit your unique needs.
 */

 Vue.mixin({
     methods : {
         printBloodBagLabel(facility_cd,donation_id,component_cd){
            let url =  'http://'+window.location.host+window.location.pathname+'label?facility_cd='+facility_cd+'&donation_id='+donation_id+'&component_cd='+component_cd;
            
            let w = window.open(url,'winname','directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=375,height=270');
            w.onload = () => {
                    w.print();
                w.close();
            };
         },

         printReleaseForm(intent_id){
            let url =  'http://'+window.location.host+window.location.pathname+'releaseform/'+intent_id;
            
            let w = window.open(url,'_blank');
            w.onload = () => {
                    w.print();
                w.close();
            };
         },

         prepareTemplate(template){
            template = template.replaceAll("{{FACILITY_NAME}}",'Department of Health');
            template = template.replaceAll("{{BARCODE}}",'<div style="background:#fff;width:100%;height:50px;text-align:center;vertical-align:middle;"><img src="images/sample-barcode.jpg" width="320" height="45" /></div>');
            template = template.replaceAll("{{ABO}}","B");
            template = template.replaceAll("{{RH}}","Positive");
            template = template.replaceAll("{{COMPONENT}}","FRESH FROZEN PLASMA");
            template = template.replaceAll("{{VOLUME}}","150");
            template = template.replaceAll("{{COLLECTION_DATE}}","January 06, 2018");
            template = template.replaceAll("{{EXPIRATION_DATE}}","January 06, 2019 23:59:00");
            template = template.replaceAll("{{STORE}}","Store at -18 to -89 &deg;C");
            template = template.replaceAll("{{ANTIBODY}}","ANTIBODY SCREEN: NEGATIVE");
            template = template.replaceAll("{{NAT}}","NUCLIEC ACID TESTING: NEGATIVE");
            template = template.replaceAll("{{ZIKA}}","ZIKA TESTING: NEGATIVE");
            return template;
         },
		 checkOwnedDonationID(that,donation_id,cb){
            let {facility_cd} = that.$session.get("user");
            that.$http.get(that,"sticker/check/"+facility_cd+"/"+donation_id)
            .then(({data}) => {
                cb(data.status);
            });
        },
        monthDiff(d1, d2) {
            let months;
            months = (d2.getFullYear() - d1.getFullYear()) * 12;
            months -= d1.getMonth() + 1;
            months += d2.getMonth();
            return months;
        },
        dateToday(){
            var today = new Date();
            var dd = today.getDate();
            var mm = today.getMonth()+1; //January is 0!
            var yyyy = today.getFullYear();

            if(dd<10) {
                dd = '0'+dd
            } 

            if(mm<10) {
                mm = '0'+mm
            } 

            today = yyyy + '-' + mm + '-' + dd;
            return today;
        },
        backupchat(){
            Window.socket.emit('backup')
        }
     }
 })


const app = new Vue({
    el: '#app',
    template: `<start></start>`,
    router,
    store,
    data : {
        messages : [] , users : []
    },
    watch : {
        messages(){
            this.$store.state.messages = this.messages;
        },
        users(){
            this.$store.state.users = this.users;
        }
    }
});

// let socket = ioClient('http://'+window.location.hostname+':3000');
let socket = ioClient('http://v2.nbbnets.net:3000');
Window.socket = socket;

if(app.$session.get('user')){
    socket.emit("add-user",app.$session.get('user').user_id);
}

socket.on('init', function(messages){
    app.messages = messages;
});

socket.on('read', function(message){
    app.messages.push(message);
});

socket.on('seen-message', function({from,to}){
    _.filter(this.messages,{from,to}).map(message => {
        message.seen = true;
    });
    app.$store.state.messages = this.messages;
});

socket.on('update-users', function(users){
    app.users = users;
    app.$store.state.users = users;
});

window.app = app;

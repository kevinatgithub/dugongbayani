<template>
  <div class="container">
      <div class="content">
        <button class="btn-success" @click="saveChanges">Save</button>
        <vue-json-editor v-model="awards" :show-btns="false" @json-change="onJsonChange"></vue-json-editor>
      </div>
  </div>
</template>

<script>
import ioClient from 'socket.io-client'
import AwardRow from './AwardRow'
import vueJsonEditor from 'vue-json-editor'

let socket = ioClient('http://122.53.179.106:3000');
let data = {
    awards : {}, nawards : {}
};
let app = {
    components : {vueJsonEditor},
    data(){
        return data;
    },
    methods : {
        onJsonChange(value){
            this.nawards = value
        },
        saveChanges(){
            let id = prompt("Enter ID");
            let award = _.find(this.nawards.data,{id : id});
            socket.emit("update",{id,award})
        }
    }
}

export default app;

socket.on('queue', function(awards){
    data.awards = awards;
});

</script>
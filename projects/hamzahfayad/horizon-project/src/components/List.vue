<template>
  <div>
      <p>Here, you can add your painting ideas. Or you can use this as a list for your upcoming projects.</p>
    <div id="form">
    <form @submit.prevent="add">
      <div class="row">
        <div class="col-md-6">
          <div class="form-group">
            <input type="text" class="form-control" placeholder="title" v-model="list.title" />
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-md-6">
          <div class="form-group">
            <textarea class="form-control" placeholder="description" v-model="list.body" rows="5"></textarea>
          </div>
        </div>
      </div>
      <br />
      <div class="form-group">
        <button class="addButton">+</button>
      </div>
    </form>
</div>
    <table id="posts">
      <tr>
        <th>Title</th>
        <th>Body</th>
        <th>Edit</th>
      </tr>
      <tr v-for="li in list" :key="li">
        <td>{{ li.title }}</td>
        <td>{{ li.body }}</td>
        <td>
          <button class="deleteButton" @click.prevent="deleteRow(li._id)">x</button>
        </td>
      </tr>
    </table>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      list: []
    };
  },
  components: {},
  created() {
    axios
      .get("http://localhost:3000")
      .then(res => {
        this.list = res.data;
      })
      .catch(err => {
        console.log(err);
      });
  },
  methods: {
    deleteRow(id) {
      axios
        .delete(`http://localhost:3000/delete/${id}`)
        .then(res => {
          //this.list.splice(id, 1)
          this.list.splice(
            this.list.findIndex(i => i._id == id),
            1
          );

          console.log(res);
        })
        .catch(err => {
          console.log(err);
        });
    },
    add() {
      axios
        .post(`http://localhost:3000/add`, {
          title: this.list.title,
          body: this.list.body
        })
        .then(response => {
          console.log(response)
          //refresh page after POST request
          window.location.reload(false); 
        })
        .catch(e => {
          this.errors.push(e);
        });
    }
  }
};
</script>

<style scoped>
p {
  font-size: 1.1em;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #141414;
  text-align: center;
  margin: 2% auto;
}
#form{
  border-radius: 5px;
  padding: 20px;
}
input, textarea{
  padding: 4px;
  margin: 1% auto;
  width: 30%;
}
.addButton, .deleteButton{
  border: none;
  color: #000000;
  padding: 4px 18px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 1.2rem;
  width: 6%;
  cursor: pointer;
}
.addButton{
    background-color: #2ae061;
}
.deleteButton{
    background-color: #e02a2a;
    width: 18%;
}
#posts {
  font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
  border-collapse: collapse;
  width: 100%;
  margin: 6% auto;
}

#posts td, #customers th {
  border: 1px solid #ddd;
  padding: 8px;
}

#posts tr:nth-child(even){background-color: #f2f2f2;}

#posts tr:hover {background-color: #ddd;}

#posts th {
  padding-top: 12px;
  padding-bottom: 12px;
  text-align: left;
  background-color: #d8734b;
  color: white;
}
</style>
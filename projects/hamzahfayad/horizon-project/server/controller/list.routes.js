const express = require('express')
const listRouter = express.Router()

const List = require('../model/list.model')

listRouter.route('/').get(function (req, res) {
    List.find(function(err, lists) {
        if(err) {
            res.json(err)
        }
        else {
            res.json(lists)
        }
    })
})

listRouter.route('/:id').get(function (req, res) {
    let id = req.params.id
    List.findById(id, function(err, list) {
        if(err) {
            res.json(err)
        }
        else {
            res.json(list)
        }
    })
})

listRouter.route('/add').post(function (req, res) {
    let list = new List(req.body)
    list.save()
    .then(() => {
        res.status(201).json({'message': 'success'})
    })
    .catch(() => {
        res.status(400).send({'message': 'error'})
    })
})

listRouter.route('/delete/:id').delete(function (req, res) {
    let id = req.params.id
    List.findByIdAndRemove(id, function(err, list) {
        if(err) {
            res.json(err)
        }
        else {
            res.json({'message': 'removed'})
        }
    })
})

module.exports = listRouter;

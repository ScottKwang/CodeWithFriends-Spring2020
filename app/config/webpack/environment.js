const { environment } = require('@rails/webpacker')

const webpack = require('webpack')
environment.plugins.append('Provide', new webpack.ProvidePlugin({
  $: 'jquery',
  jQuery: 'jquery',
  jquery: 'jquery',
  Popper: ['popper.js', 'default'],
  Rails: '@rails/ujs',
  moment: 'moment',
  bootstrap: 'bootstrap',
}))

module.exports = environment
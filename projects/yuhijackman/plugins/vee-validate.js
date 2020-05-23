// https://logaretm.github.io/vee-validate/guide/basics.html

import { extend } from "vee-validate";
import { required, email } from "vee-validate/dist/rules";
import moment from 'moment'

extend("required", {
  ...required,
  message: "This field is required"
})

extend("email", {
    ...email,
    message: "This field must be a valid email."
})

extend("date_format", {
    params: ['date_format'],
    validate(value) {
        let today = new Date();
        let date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate()
        return value && moment(value, "YYYY-MM-DD", true).isValid() && moment(value).isSameOrAfter('1990-01-01') && moment(value).isSameOrBefore(date)
    },
    message: "This field must be a valid email. YYYY/MM/DD."
})

extend("min", {
    params: ['min'],
    validate(value, { min }) {
        return value.length >= min
    },
    message: "Password needs to be at least {min} characters."
})

extend('password', {
    params: ['target'],
    validate(value, { target }) {
      return value === target
    },
    message: 'Password confirmation does not match'
});

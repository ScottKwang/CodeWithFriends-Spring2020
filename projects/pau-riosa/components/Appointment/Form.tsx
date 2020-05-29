/**
 * @format
 * @flow strict-local
 */
import React, {useState} from 'react';
import {Image, StyleSheet, Text, View, Dimensions, TouchableOpacity, TextInput} from 'react-native';
import {Label, Input, Button, Icon} from 'react-native-elements';
import CustomDatePicker from '../CustomDatePicker';
import {colors} from '../../assets';

export interface IProps {
  navigation: any;
}

interface IState {}

export class AppointmentForm extends React.Component<IProps, IState> {
  state = {
    dateString: "",
    date: new Date(),
    show: false
  };
  onChange = (event: any, selectedDate: any) => {
    this.setState({dateString: selectedDate.toString(), date: selectedDate})
  }

  render() {
    return (     
      <View style={{flex: 1, flexDirection: 'column'}}>
        <Text style={styles.labelStyle}>Name</Text>
        <Input
          inputStyle={styles.inputStyle}
          inputContainerStyle={styles.inputContainerStyle}
        />
        <Text style={styles.labelStyle}>Agenda</Text>
        <Input
          inputStyle={styles.inputStyle}
          inputContainerStyle={styles.inputContainerStyle}
        />
        <Text style={styles.labelStyle}>Date</Text>
        <CustomDatePicker />
        <Text style={styles.labelStyle}>Description</Text>
        <Input
          multiline={true}
          inputStyle={styles.inputStyle}
          inputContainerStyle={[styles.inputContainerStyle, {height: 200}]}
        />
        <Button title="Submit" buttonStyle={styles.buttonStyle} />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  datepickerStyle: {
    width: '100%',
    paddingHorizontal: 10,
    marginVertical: 10,
    alignSelf: 'center',
  },
  datepickerPlaceholderText: {
    fontFamily: 'Gill Sans',
    fontSize: 18,
    paddingHorizontal: 10,
    alignSelf: 'flex-start',
  },
  datepickerDateInput: {
    fontFamily: 'Gill Sans',
    fontSize: 100,
    borderWidth: 1,
    borderColor: '#CAD3DF',
    height: 50,
    borderRadius: 5,
  },
  inputStyle: {
    fontFamily: 'Gill Sans',
    marginHorizontal: 10,
    color: '#363A44',
  },
  inputContainerStyle: {
    borderWidth: 1,
    borderColor: '#CAD3DF',
    borderRadius: 5,
    marginVertical: 10,
    paddingRight: 10,
    height: 50,
  },
  containerStyle: {
    backgroundColor: '#FFFFFF', 
    shadowColor: '#CDCDCD', 
    shadowRadius: 5, 
    shadowOffset: {width: 3, height: 5},
  },
  mainText: {
    marginHorizontal: 10,
    marginVertical: 20,
    fontFamily: 'Gill Sans',
    fontSize: 25,
  },
  labelStyle: {
    marginHorizontal: 10,
    fontFamily: 'Gill Sans',
    fontSize: 16,
  },
  buttonStyle: {
    borderRadius: 5,
    backgroundColor: colors.ocean1,
    paddingVertical: 15,
    marginHorizontal: 10,
    marginVertical: 20,
  }
})

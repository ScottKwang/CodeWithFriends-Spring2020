/**
 * @format
 * @flow strict-local
 */
import * as React from 'react';
import {
  Dimensions,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  View,
  Text,
  StatusBar,
  Image
} from 'react-native';
import CustomDatePicker from '../../components/CustomDatePicker';
import { Input, Button, Icon } from 'react-native-elements'
import {colors} from '../../assets'

export interface IProps {
  navigation: any;
}
export interface IState {}

export default class EditProfileScreen extends React.Component<IProps, IState> {
  render() {
    return (
      <View style={{flex: 1, flexDirection: 'column'}}>
        <Text style={styles.labelStyle}>First Name</Text>
        <Input
          inputStyle={styles.inputStyle}
          inputContainerStyle={styles.inputContainerStyle}
        />
        <Text style={styles.labelStyle}>Surname</Text>
        <Input
          inputStyle={styles.inputStyle}
          inputContainerStyle={styles.inputContainerStyle}
        />
        <Text style={styles.labelStyle}>Birthdate</Text>
        <CustomDatePicker />
        <Button title="Save" buttonStyle={styles.buttonStyle} />
      </View>
    );
  } 
};

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

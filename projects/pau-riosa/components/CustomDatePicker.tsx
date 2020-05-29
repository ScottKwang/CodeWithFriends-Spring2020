/**
 * @format
 * @flow strict-local
 */
import React from 'react';
import {Platform, Text, StyleSheet, TextInput, View, TouchableOpacity} from 'react-native';
import {Overlay, Header} from 'react-native-elements';
import DateTimePicker from '@react-native-community/datetimepicker';
import moment from "moment";

export interface IProps {
  date?: string;
  placeholder?: string;
}

export interface IState {
  dateString: string;
  date: string;
  show: boolean;
}

class CustomDatePicker extends React.Component<IProps, IState> {
  state = {
    dateString: moment(new Date()).format('YYYY-MM-DD'),
    date: this.props.date || new Date(),
    show: false
  };

  onChange = (event: any, selectedDate: any) => {
    console.log(selectedDate)
    this.setState({dateString: moment(selectedDate).format('YYYY-MM-DD'), date: selectedDate})
  }

  showOverlay = () => {
    this.setState({ show: true}) 
  }

  hideOverlay = () => {
    this.setState({ show: false}) 
  }
  render() {
    return (
      <View style={{ flex: 1, borderRadius: 100}}> 
        <TouchableOpacity onPress={this.showOverlay} style={styles.inputContainerStyle}>
          {this.state.dateString ? (
            <Text style={styles.textStyle}>{this.state.dateString}</Text>
          ) : (
            <Text style={styles.placeholderStyle}>{this.props.placeholder}</Text>
          )} 
        </TouchableOpacity>
        {Platform.OS === 'ios' ? (
          <Overlay isVisible={this.state.show} onBackdropPress={this.hideOverlay} overlayStyle={styles.overlayStyle}>
            <>
            <View style={styles.headerStyle}>
              <TouchableOpacity onPress={this.hideOverlay}>
                <Text style={{ paddingHorizontal: 15 }}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity onPress={this.hideOverlay}>
                <Text style={{ paddingHorizontal: 15, color: 'green' }}>Done</Text>
              </TouchableOpacity>
            </View>
            <DateTimePicker
              value={this.state.date}
              mode={'date'}
              is24Hour={true}
              display="default"
              onChange={this.onChange}
              style={{ backgroundColor: 'white' }}
            />
            </>
          </Overlay>
        ) : (
          <>
            {this.state.show && 
            <DateTimePicker
              value={this.state.date}
              mode={'date'}
              is24Hour={true}
              display="default"
              onChange={this.onChange}
              style={{ backgroundColor: 'white' }}
            />
            }
          </>
        )}
      </View>
    );
  } 
};
export default CustomDatePicker;
const styles = StyleSheet.create({
  overlayStyle: {
    flex: 1, 
    width: '100%', 
    justifyContent: 'flex-end',  
    backgroundColor: '#00000066',
  },
  headerStyle: {
    backgroundColor: 'white', 
    borderTopLeftRadius: 10, 
    borderTopRightRadius: 10,  
    borderColor: '#CDCDCD', 
    borderBottomWidth: 1, 
    height: 50, 
    justifyContent: 'space-between', 
    alignItems: 'center', 
    flexDirection: 'row', 
  },
 inputContainerStyle: {
    alignItems: 'flex-start',
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: '#CAD3DF',
    borderRadius: 5,
    marginVertical: 10,
    marginHorizontal: 10,
    paddingRight: 10,
    height: 50,
  },
  placeholderStyle: {
    fontFamily: 'Gill Sans',
    fontSize: 16,
    color: '#CDCDCD',
    marginHorizontal: 10,
  },
  textStyle: {
    fontFamily: 'Gill Sans',
    fontSize: 16,
    marginHorizontal: 10,
  }
})

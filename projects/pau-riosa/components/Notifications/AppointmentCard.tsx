/**
 * @format
 * @flow strict-local
 */
import React from 'react';
import {Image, StyleSheet, Text, View, Dimensions, TouchableOpacity} from 'react-native';
import {Card, ListItem, Button, Icon} from 'react-native-elements';
import {colors} from '../../assets';

export interface IProps {
  navigation: any;
}

interface IState {}
export class AppointmentCard extends React.Component<IProps, IState> {
  render() {
    return (    
      <TouchableOpacity
        onPress={() => this.props.navigation.navigate('APPOINTMENT', { screen: 'APPOINTMENT_INFO'})}
      >
        <View style={styles.containerStyle}>
          <View style={styles.leftContainer}>
            <View style={styles.logoContainerStyle}>
              <Icon name='calendar' type='feather' size={30} iconStyle={styles.iconStyle} />
            </View>
          </View>
          <View style={styles.rightContainer}>
            <Text style={styles.subStyle}>
              You have a new appointment with 
            </Text>
            <Text style={styles.titleStyle}>
              Juan Dela Cruz 
            </Text>
            <Text style={styles.subStyle}>
              on Tuesday, May 20 2020 
            </Text>
          </View>
        </View>
      </TouchableOpacity> 
    );
  }
}

const styles = StyleSheet.create({
  iconStyle: {
    color: colors.white,
    paddingVertical: 10, 
    paddingHorizontal: 10,
  },
  logoContainerStyle: {
    backgroundColor: '#FB9FB0',
    width: 50,
    height: 50,
    borderRadius: 100,
  },
  containerStyle: {
    backgroundColor: colors.ocean5,
    borderColor: '#CDCDCD',
    borderWidth: 0.5,
    padding: 12,
    justifyContent: 'center',
    alignItems: 'center',
    flex: 1,
    flexDirection: 'row',
  },
  leftContainer: {
    width: '20%',
  },
  rightContainer: { 
    width: '80%',
  },
  titleStyle: {
    fontFamily: 'Gill Sans', 
    fontSize: 18, 
    color: colors.black,
  },
  subStyle: {
    fontFamily: 'Gill Sans', 
    fontSize: 15, 
    color: colors.black,
  },
})

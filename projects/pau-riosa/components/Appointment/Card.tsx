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
  constructor(props: IProps) {
    super(props);
  }

  render() {
    return (     
      <TouchableOpacity
        onPress={() => this.props.navigation.navigate('APPOINTMENT', { screen: 'APPOINTMENT_INFO'})}
      >
        <Card containerStyle={styles.containerStyle}>
          <View style={{flexDirection: 'row', flexWrap: 'wrap'}}>
            <View style={styles.leftContainer}>
              <Text style={{marginBottom: 10, fontFamily: 'Roboto', fontSize: 10, color: colors.black}}>
                TOOTH EXTRACTION 
              </Text>
              <Text style={{marginBottom: 10, fontFamily: 'Roboto-Regular', fontSize: 20, color: colors.black}}>
                Juan Dela Cruz 
              </Text>
              <Text style={{marginBottom: 10, fontFamily: 'Roboto-Regular', fontSize: 14, color: colors.black}}>
                2020-04-29   
              </Text>
            </View>
            <View style={styles.rightContainer}>
              <Image
                style={{ width: 100, height: 100, borderRadius: 5}}
                source={{ uri: 'https://images.unsplash.com/photo-1527980965255-d3b416303d12?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1000&q=80' }}
              />
            </View>
          </View>
          <View style={styles.rowDirection}>
            <Button title="VIEW DETAILS" type="outline" titleStyle={styles.titleStyle} buttonStyle={styles.buttonStyle} />
          </View>
        </Card>
      </TouchableOpacity> 
    );
  }
}

const styles = StyleSheet.create({
  containerStyle: {
    backgroundColor: '#FFFFFF', 
    shadowColor: '#CDCDCD', 
    shadowRadius: 5, 
    shadowOffset: {width: 3, height: 5},
  },
  leftContainer: {
    padding: 1, 
    width: '70%',
  },
  rightContainer: { 
    padding: 1, 
    width: '30%',
  },
  rowDirection: {
    flexDirection: 'row',
  },
  titleStyle: {
    fontFamily: 'Roboto-Medium',
    fontSize: 14,
    color: colors.ocean1,
  },
  buttonStyle: {
    borderWidth: 0, 
  },
})

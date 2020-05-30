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
import { Input, Button, Icon } from 'react-native-elements'
import {colors} from '../../../assets'
import { AppointmentCard } from '../../../components/Appointment/Card';

export interface IProps {
  navigation: any;
}
export interface IState {}

class AppointmentAttachmentScreen extends React.Component<IProps, IState> {
  render() {
    return (
      <View style={{ flex: 1 }}> 
        <StatusBar barStyle="dark-content" backgroundColor={colors.ocean1}  />
        <SafeAreaView style={{ flex: 1, backgroundColor: colors.ocean5 }}>
          <ScrollView showsVerticalScrollIndicator={false}>
            <View style={styles.scrollViewVerticalStyle}>
              <Button title="Add Attachment" titleStyle={styles.titleStyle} buttonStyle={styles.buttonStyle}/>
            </View>
          </ScrollView>
        </SafeAreaView>
      </View>
    );
  } 
};
export default AppointmentAttachmentScreen;
const deviceWidth = Dimensions.get('window').width;
const deviceHeight = Dimensions.get('window').height;
const styles = StyleSheet.create({
  titleStyle: {
    fontFamily: 'Gill Sans',
    fontSize: 16,
  },
  buttonStyle: {
    alignSelf: 'flex-end',
    borderRadius: 5,
    backgroundColor: colors.ocean1,
    paddingHorizontal: 10,
  },
  scrollViewVerticalStyle: {
    backgroundColor: colors.ocean5,
    flex: 1,
    flexDirection: 'column',
    padding: 20,
  },
  mainText: {
    fontFamily: 'Gill Sans',
    fontSize: 50,
    fontWeight: '300',
    color: '#363A44'
  },
  subText: {
    marginHorizontal: 10, 
    fontFamily: 'Gill Sans',
    fontSize: 18,
    fontWeight: '500',
    color: '#363A44'
  },
  text: {
    fontFamily: 'Gill Sans', 
    fontSize: 17, 
    marginVertical: 2 
  }
});


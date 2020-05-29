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
import {colors} from '../../assets'
import { MessageCard } from '../../components/Notifications/MessageCard';
import { AppointmentCard } from '../../components/Notifications/AppointmentCard';

export interface IProps {
  navigation: any;
}
export interface IState {}

class NotificationScreen extends React.Component<IProps, IState> {
  render() {
    return (
      <View style={{ flex: 1 }}> 
        <StatusBar barStyle="dark-content" backgroundColor={colors.ocean1}  />
        <SafeAreaView style={{ flex: 1, backgroundColor: colors.ocean5 }}>
          <View style={styles.verticalContainer}>
            <ScrollView showsVerticalScrollIndicator={false}>
              <View style={styles.scrollViewVerticalStyle}>
                <MessageCard {...this.props}/> 
                <AppointmentCard {...this.props}/> 
                <MessageCard {...this.props}/> 
                <AppointmentCard {...this.props}/> 
                <AppointmentCard {...this.props}/> 
                <MessageCard {...this.props}/> 
                <MessageCard {...this.props}/> 
                <AppointmentCard {...this.props}/> 
                <AppointmentCard {...this.props}/> 
                <AppointmentCard {...this.props}/> 
                <MessageCard {...this.props}/> 
                <AppointmentCard {...this.props}/> 
                <MessageCard {...this.props}/> 
                <MessageCard {...this.props}/> 
              </View>
            </ScrollView>
          </View>
        </SafeAreaView>
      </View>
    );
  } 
};
export default NotificationScreen;
const deviceWidth = Dimensions.get('window').width;
const deviceHeight = Dimensions.get('window').height;
const styles = StyleSheet.create({
  verticalContainer: {
    justifyContent: "center",
    alignItems: "center",
    flex: 1, 
    backgroundColor: "#e5e5e5",
  },
  horizontalContainer: {
    height: '30%', 
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#e5e5e5",
  },
  headerText: {
    fontSize: 30,
    textAlign: "center",
    margin: 10,
    color: 'white',
    fontWeight: "bold"
  },
  scrollViewVerticalStyle: {
    backgroundColor: colors.ocean5,
    flex: 1,
    flexDirection: 'column'
  },
  logoContainer: {
    flex: 0.2,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: colors.ocean5
  },
  flex1: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'space-evenly',
    paddingHorizontal: 15,
    backgroundColor: colors.ocean5
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


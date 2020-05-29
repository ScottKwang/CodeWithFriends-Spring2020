/**
 * @format
 * @flow strict-local
 */
import React, { useState } from 'react';
import {StyleSheet, View, Text} from 'react-native';
import {Button, Icon, Overlay} from 'react-native-elements';
// Screens
import AppointmentScreen from '../screens/Root/Appointments/AppointmentScreen';
import NotificationScreen from '../screens/Root/NotificationScreen';
import SettingScreen from '../screens/Settings/SettingScreen';
// colors
import {colors} from '../assets';
// navigation
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
// components
import {CustomModal} from '../components/CustomModal';
import {AppointmentForm} from '../components/Appointment/Form';
type NavigatorParams = {
  APPOINTMENT_LIST: undefined;
  NOTIFICATION: undefined;
  SETTINGS: undefined;
  NEW_APPOINTMENT: undefined;
};
const BottomTab = createBottomTabNavigator<NavigatorParams>();
export default function Root() {

  const [visible, setVisible] = useState(false);

  const toggleOverlay = () => {
    setVisible(!visible);
  }
  return (
    <>
    <CustomModal
      title="Create Appointment"
      children={<AppointmentForm />} 
      visible={visible} 
      onBackdropPress={toggleOverlay} 
      overlayStyle={{ width: '90%', borderRadius: 5}} 
    />
    <BottomTab.Navigator
      initialRouteName="APPOINTMENT_LIST"
      tabBarOptions={{
        activeTintColor: colors.ocean1,
        inactiveTintColor: colors.primaryGrey,
      }}>
      <BottomTab.Screen
        name="APPOINTMENT_LIST"
        component={AppointmentScreen}
        options={{
          tabBarLabel: '',
          tabBarIcon: ({color, size}) => (
            <Icon name="calendar" size={size} type="feather" color={color} />
          ),
        }}
      />
      <BottomTab.Screen
        name="NOTIFICATION"
        component={NotificationScreen}
        options={{
          tabBarLabel: '',
          tabBarIcon: ({color, size}) => (
            <Icon name="bell" size={size} type="feather" color={color} />
          ),
        }}
      />
      <BottomTab.Screen
        name="SETTINGS"
        component={SettingScreen}
        options={{
          tabBarLabel: '',
          tabBarIcon: ({color, size}) => (
            <Icon
              name="settings"
              size={size}
              type="material-community-icons"
              color={color}
            />
          ),
        }}
      />
      <BottomTab.Screen
        name="NEW_APPOINTMENT"
        component={AppointmentScreen}
        options={{
          tabBarButton: () => (
            <Button
              icon={
                <Icon
                  name="apps"
                  type="material-community-icons"
                  size={25}
                  color="white"
                />
              }
              onPress={toggleOverlay}
              buttonStyle={styles.buttonStyle}
            />
          ),
        }}
      />
    </BottomTab.Navigator>
    </>
  );
}


const styles = StyleSheet.create({
  viewStyle: {
    width: 24,
    height: 24,
    margin: 5,
  },
  notificationStyle: {
    position: 'absolute',
    right: -6,
    top: -3,
    backgroundColor: colors.primaryBlue,
    borderRadius: 10,
    width: 15,
    height: 15,
    justifyContent: 'center',
    alignItems: 'center',
  },
  buttonStyle: {
    height: 56,
    width: 56,
    bottom: 30,
    marginRight: 20,
    marginLeft: 140,
    borderRadius: 100,
    borderWidth: 5,
    backgroundColor: colors.ocean1,
    borderColor: colors.ocean1,
    shadowColor: colors.ocean1,
    shadowOpacity: 1,
    shadowRadius: 7,
    shadowOffset: {width: 0, height: 5},
  },
});

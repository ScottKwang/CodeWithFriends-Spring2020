/**
 * @format
 */
import * as React from 'react';
import { createMaterialTopTabNavigator } from '@react-navigation/material-top-tabs';
import {Icon} from 'react-native-elements';
// Screens
import AppointmentMessageScreen from '../screens/Root/Appointments/AppointmentMessageScreen';
import AppointmentInfoScreen from '../screens/Root/Appointments/AppointmentInfoScreen';
import AppointmentAttachmentScreen from '../screens/Root/Appointments/AppointmentAttachmentScreen';
// colors
import {colors} from '../assets';
type NavigatorParams = {
  APPOINTMENT_INFO: undefined;
  APPOINTMENT_MESSAGE: undefined;
  APPOINTMENT_ATTACHMENT: undefined;
};
const TopTab = createMaterialTopTabNavigator<NavigatorParams>();
export default function Appointment() {
  return (
    <TopTab.Navigator 
      initialRouteName="APPOINTMENT_INFO"
      tabBarOptions={{
        showIcon: true,
        showLabel: false,
        activeTintColor: colors.ocean1,
        inactiveTintColor: colors.primaryGrey,
      }}
    >
      <TopTab.Screen
        name="APPOINTMENT_INFO"
        component={AppointmentInfoScreen}
        options={{ 
          tabBarIcon: ({color}) => (
            <Icon name="user-circle" size={24} type="font-awesome" color={color} />
          ),
        }}
      />
      <TopTab.Screen
        name="APPOINTMENT_MESSAGE"
        component={AppointmentMessageScreen}
        options={{ 
          tabBarIcon: ({color}) => (
            <Icon name="chat-bubble" size={24} type="material-community-icon" color={color} />
          ),
        }}
      />
      <TopTab.Screen
        name="APPOINTMENT_ATTACHMENT"
        component={AppointmentAttachmentScreen}
        options={{ 
          tabBarIcon: ({color}) => (
            <Icon name="folder-open" size={24} type="font-awesome" color={color} />
          ),
        }}
      />
    </TopTab.Navigator>
  );
}

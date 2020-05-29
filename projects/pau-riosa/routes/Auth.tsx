/**
 * @format
 * @flow strict-local
 */
import * as React from 'react';
// Screens
import HomeScreen from '../screens/Auth/HomeScreen';
import ForgotPasswordScreen from '../screens/Auth/ForgotPasswordScreen';
import LoginScreen from '../screens/Auth/LoginScreen';
import SignUpScreen from '../screens/Auth/SignUpScreen';
// colors
import {colors} from '../assets';
// navigator
import {createStackNavigator} from '@react-navigation/stack';
const Stack = createStackNavigator();

export default function Auth() {
  return (
    <Stack.Navigator initialRouteName="HOME">
      <Stack.Screen
        options={{
          headerShown: false,
        }}
        name="HOME"
        component={HomeScreen}
      />
      <Stack.Screen
        options={{
          headerShown: true,
          headerTitle: '',
          headerStyle: {backgroundColor: colors.ocean5},
        }}
        name="LOGIN"
        component={LoginScreen}
      />
      <Stack.Screen
        options={{
          headerShown: true,
          headerTitle: '',
          headerStyle: {backgroundColor: colors.ocean5},
        }}
        name="SIGN UP"
        component={SignUpScreen}
      />
      <Stack.Screen
        options={{
          headerShown: true,
          headerTitle: '',
          headerStyle: {backgroundColor: colors.ocean5},
        }}
        name="FORGOT PASSWORD"
        component={ForgotPasswordScreen}
      />
    </Stack.Navigator>
  );
}

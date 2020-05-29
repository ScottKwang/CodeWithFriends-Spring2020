import 'react-native-gesture-handler';
import * as React from 'react';
import { SafeAreaView } from 'react-native';
import Auth from './routes/Auth';
import Root from './routes/Root';
import Appointment from './routes/Appointment';
// colors
import {colors} from './assets'
// navigation 
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

type NavigatorParams = {
  AUTH: undefined,
  ROOT: undefined,
  APPOINTMENT: undefined,

}
const isSignedIn = true;
const Stack = createStackNavigator<NavigatorParams>();
function StackNavigator() {
  return(
    <Stack.Navigator
      initialRouteName="AUTH"
      screenOptions={{
        headerStyle: { backgroundColor: colors.ocean1 },
      }}
    >
    {isSignedIn ? (
      <>
        <Stack.Screen
          options={{ 
            headerShown: true,
            headerTitle: ''
          }}
          name="ROOT" 
          component={Root} 
        />
        <Stack.Screen 
          options={{ 
            headerShown: true,
            headerTitle: ''
          }} 
          name="APPOINTMENT" 
          component={Appointment} 
        />
      </>
      ) : (
      <Stack.Screen 
        options={{ 
          headerShown: false 
        }} 
        name="AUTH" 
        component={Auth} 
      />
    )}
    </Stack.Navigator>
  );
}

export default function App() {
  return (
    <NavigationContainer>
      <SafeAreaView style={{ flex: 0, backgroundColor: colors.ocean1 }} />
      <StackNavigator />
    </NavigationContainer>
  );
}


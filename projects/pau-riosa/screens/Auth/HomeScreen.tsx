/**
 * @format
 * @flow strict-local
 */
import * as React from 'react';
import {SafeAreaView, View, Text, StatusBar} from 'react-native';
import {Button, Icon} from 'react-native-elements';
import {home} from './styles';
import {colors} from '../../assets';
export interface IProps {
  navigation: any;
}

export interface IState {}

class HomeScreen extends React.Component<IProps, IState> {
  render() {
    return (
      <View style={home.flex1Container}>
        <StatusBar barStyle="dark-content" backgroundColor={colors.ocean1} />
        <SafeAreaView style={home.safeAreaView}>
          <View style={home.flex6}>
            <Icon
              raised
              name="tooth"
              size={45}
              type="material-community"
              color={colors.ocean1}
            />
            <Text style={home.mainText}>Twinkle Teeth</Text>
          </View>
          <View style={home.flex1}>
            <Button
              titleStyle={home.subText}
              type={'outline'}
              buttonStyle={home.loginButton}
              title="Login"
              onPress={() => this.props.navigation.navigate('LOGIN')}
            />
            <Button
              titleStyle={home.subText}
              type={'outline'}
              buttonStyle={home.signUpButton}
              title="Sign up"
              onPress={() => this.props.navigation.navigate('SIGN UP')}
            />
          </View>
        </SafeAreaView>
      </View>
    );
  }
}

export default HomeScreen;

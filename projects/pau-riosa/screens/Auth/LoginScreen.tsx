/**
 * @format
 * @flow strict-local
 */
import * as React from 'react';
import {SafeAreaView, View, Text, StatusBar} from 'react-native';
import {Input, Button, Icon} from 'react-native-elements';
import {login} from './styles';
import {colors} from '../../assets';

export interface IProps {
  navigation: any;
}
export interface IState {}

class LoginScreen extends React.Component<IProps, IState> {
  render() {
    return (
      <View style={login.flex1Container}>
        <StatusBar barStyle="dark-content" backgroundColor={colors.ocean1} />
        <SafeAreaView style={login.safeAreaView}>
          <View style={login.logoContainer}>
            <Icon
              raised
              name="tooth"
              size={45}
              type="material-community"
              color={colors.ocean1}
            />
            <Text style={login.mainText}>Twinkle Teeth</Text>
          </View>
          <View style={login.flex1}>
            <View style={login.flex1Center}>
              <Text style={[login.subText, login.textAlignCenter]}>LOGIN</Text>
              <Input
                placeholder="Email"
                inputStyle={login.inputStyle}
                inputContainerStyle={login.inputContainerStyle}
                leftIcon={<Icon name="email" size={24} color={colors.ocean1} />}
              />
              <Input
                placeholder="Password"
                secureTextEntry={true}
                inputStyle={login.inputStyle}
                inputContainerStyle={login.inputContainerStyle}
                leftIcon={<Icon name="lock" size={24} color={colors.ocean1} />}
              />
              <Button
                titleStyle={[login.subText, {color: colors.white}]}
                buttonStyle={login.buttonStyle}
                title="LOGIN"
                onPress={() => this.props.navigation.navigate('ROOT')}
              />
            </View>
            <View style={login.flex1AlignCenter}>
              <Text
                style={[{color: colors.primaryBlue}, login.text]}
                onPress={() =>
                  this.props.navigation.navigate('FORGOT PASSWORD')
                }>
                Forgot your password?
              </Text>
              <Text style={[login.text]}>
                Dont'have an account?{' '}
                <Text
                  style={{color: colors.primaryBlue}}
                  onPress={() => this.props.navigation.navigate('SIGN UP')}>
                  Sign Up
                </Text>
              </Text>
            </View>
          </View>
        </SafeAreaView>
      </View>
    );
  }
}
export default LoginScreen;

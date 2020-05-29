/**
 * @format
 * @flow strict-local
 */
import * as React from 'react';
import {SafeAreaView, View, Text, StatusBar} from 'react-native';
import {Input, Button, Icon} from 'react-native-elements';
import {colors} from '../../assets';
import {forgotpassword} from './styles';

export interface IProps {
  navigation: any;
}
export interface IState {}

class ForgotPasswordScreen extends React.Component<IProps, IState> {
  render() {
    return (
      <View style={forgotpassword.flex1Container}>
        <StatusBar barStyle="dark-content" backgroundColor={colors.ocean1} />
        <SafeAreaView style={forgotpassword.safeAreaView}>
          <View style={forgotpassword.logoContainer}>
            <Icon
              raised
              name="tooth"
              size={45}
              type="material-community"
              color={colors.ocean1}
            />
            <Text style={forgotpassword.mainText}>Twinkle Teeth</Text>
          </View>
          <View style={forgotpassword.flex1}>
            <Text style={[forgotpassword.subText, forgotpassword.subTextStyle]}>
              Reset Password
            </Text>
            <Input
              placeholder="Email"
              inputStyle={forgotpassword.inputStyle}
              inputContainerStyle={forgotpassword.inputContainerStyle}
              leftIcon={<Icon name="email" size={24} color={colors.ocean1} />}
            />
            <Button
              titleStyle={forgotpassword.buttonText}
              buttonStyle={forgotpassword.buttonStyle}
              title="RESET PASSWORD"
              onPress={() => this.props.navigation.navigate('FORGOT PASSWORD')}
            />
          </View>
        </SafeAreaView>
      </View>
    );
  }
}
export default ForgotPasswordScreen;

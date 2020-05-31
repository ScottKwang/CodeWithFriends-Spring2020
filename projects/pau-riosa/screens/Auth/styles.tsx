/**
 * @format
 * @flow strict-local
 */
import {StyleSheet} from 'react-native';
import {colors} from '../../assets';

export const forgotpassword = StyleSheet.create({
  flex1: {
    flex: 1,
  },
  flex1Container: {
    flex: 1,
    flexDirection: 'column',
    marginHorizontal: 5,
    backgroundColor: colors.ocean5,
  },
  safeAreaView: {
    flex: 1,
    backgroundColor: colors.ocean5,
  },
  subTextStyle: {
    alignSelf: 'center',
    fontSize: 25,
    marginVertical: 30,
  },
  subText: {
    marginHorizontal: 10,
    fontFamily: 'Gill Sans',
    fontSize: 18,
    fontWeight: '400',
    color: colors.black,
  },
  buttonText: {
    marginHorizontal: 10,
    fontFamily: 'Gill Sans',
    fontSize: 16,
    fontWeight: '500',
  },
  text: {
    fontFamily: 'Gill Sans',
    fontSize: 17,
    marginVertical: 2,
  },
  mainText: {
    fontFamily: 'Gill Sans',
    fontSize: 50,
    fontWeight: '400',
    color: colors.black,
  },
  logoContainer: {
    flex: 0.2,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: colors.ocean5,
  },
  inputStyle: {
    fontFamily: 'Gill Sans',
    marginHorizontal: 10,
    color: colors.black,
  },
  inputContainerStyle: {
    borderWidth: 1,
    borderColor: '#CAD3DF',
    borderRadius: 5,
    marginVertical: 10,
    paddingRight: 10,
    height: 50,
  },
  buttonStyle: {
    height: 60,
    borderWidth: 1,
    marginHorizontal: 10,
    marginVertical: 10,
    borderRadius: 5,
    borderColor: colors.ocean1,
    backgroundColor: colors.ocean1,
  },
});
export const signup = StyleSheet.create({
  text: {
    fontFamily: 'Gill Sans',
    fontSize: 17,
    marginVertical: 2,
  },
  mainText: {
    fontFamily: 'Gill Sans',
    fontSize: 50,
    fontWeight: '400',
    color: '#363A44',
  },
  flex6: {
    flex: 5,
    flexDirection: 'column',
    marginHorizontal: 10,
    marginVertical: 5,
    backgroundColor: colors.ocean5,
  },
  flex1Column: {
    flex: 1,
    flexDirection: 'column',
  },
  scrollViewBackgroundColor: {
    backgroundColor: colors.ocean5,
  },
  safeAreaView: {
    flex: 1,
    backgroundColor: colors.ocean1,
  },
  flex1: {
    flex: 1,
  },
  subText: {
    fontFamily: 'Gill Sans',
    fontSize: 15,
    fontWeight: '400',
    color: '#ffffff',
  },
  subTextStyle: {
    alignSelf: 'center',
    fontSize: 25,
    marginVertical: 30,
    color: '#363A44',
  },
  logoContainer: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: colors.ocean5,
    marginTop: 20,
  },
  inputStyle: {
    fontFamily: 'Gill Sans',
    marginHorizontal: 10,
    color: '#363A44',
  },
  inputContainerStyle: {
    borderWidth: 1,
    borderColor: '#CAD3DF',
    borderRadius: 5,
    marginVertical: 10,
    paddingRight: 10,
    height: 50,
  },
  datepickerStyle: {
    width: '100%',
    paddingHorizontal: 10,
    marginVertical: 10,
    alignSelf: 'center',
  },
  datepickerPlaceholderText: {
    fontFamily: 'Gill Sans',
    fontSize: 18,
    paddingHorizontal: 10,
    alignSelf: 'flex-start',
  },
  datepickerDateInput: {
    fontFamily: 'Gill Sans',
    fontSize: 100,
    borderWidth: 1,
    borderColor: '#CAD3DF',
    height: 50,
    borderRadius: 5,
  },
  checkBoxContainerStyle: {
    backgroundColor: '#ffffff',
    borderColor: '#ffffff',
  },
  buttonStyle: {
    height: 60,
    borderWidth: 1,
    marginHorizontal: 10,
    marginVertical: 10,
    borderRadius: 5,
    borderColor: colors.ocean1,
    backgroundColor: colors.ocean1,
  },
});

export const login = StyleSheet.create({
  flex1: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'space-evenly',
    marginHorizontal: 5,
    backgroundColor: colors.ocean5,
  },
  mainText: {
    fontFamily: 'Gill Sans',
    fontSize: 50,
    fontWeight: '400',
    color: '#363A44',
  },
  text: {
    fontFamily: 'Gill Sans',
    fontSize: 17,
    marginVertical: 2,
  },
  flex1Container: {
    flex: 1,
  },
  textAlignCenter: {
    alignSelf: 'center',
    fontSize: 20,
    marginVertical: 30,
  },
  flex1Center: {
    flex: 1,
    justifyContent: 'center',
    marginBottom: 10,
    padding: 5,
  },
  flex1AlignCenter: {
    flex: 1,
    alignItems: 'center',
  },
  subText: {
    marginHorizontal: 10,
    fontFamily: 'Gill Sans',
    fontSize: 16,
    fontWeight: '400',
    color: '#363A44',
  },
  safeAreaView: {
    flex: 1,
    backgroundColor: colors.ocean5,
  },
  logoContainer: {
    flex: 0.2,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: colors.ocean5,
  },
  inputStyle: {
    fontFamily: 'Gill Sans',
    marginHorizontal: 10,
  },
  inputContainerStyle: {
    borderWidth: 1,
    borderColor: '#CAD3DF',
    borderRadius: 5,
    marginVertical: 10,
    height: 50,
  },
  buttonStyle: {
    height: 60,
    borderWidth: 1,
    marginHorizontal: 10,
    marginVertical: 10,
    borderRadius: 5,
    borderColor: colors.ocean1,
    backgroundColor: colors.ocean1,
  },
});

export const home = StyleSheet.create({
  flex1Container: {
    flex: 1,
  },
  safeAreaView: {
    flex: 1,
    backgroundColor: colors.ocean1,
  },
  signUpButton: {
    height: 70,
    borderRadius: 5,
    borderWidth: 1,
    borderColor: '#363A44',
  },
  loginButton: {
    height: 70,
    borderWidth: 1,
    borderRadius: 5,
    borderColor: colors.ocean5,
    backgroundColor: colors.ocean5,
  },
  flex6: {
    flex: 6,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: colors.ocean1,
  },
  flex1: {
    flex: 1.5,
    flexDirection: 'column',
    justifyContent: 'space-evenly',
    paddingHorizontal: 15,
    backgroundColor: colors.ocean1,
  },
  mainText: {
    fontFamily: 'Gill Sans',
    fontSize: 50,
    fontWeight: '400',
    color: '#363A44',
  },
  subText: {
    marginHorizontal: 10,
    fontFamily: 'Gill Sans',
    fontSize: 18,
    fontWeight: '500',
    color: '#363A44',
  },
});

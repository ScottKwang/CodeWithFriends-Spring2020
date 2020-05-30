/**
 * @format
 */

import 'react-native';
import { shallow } from 'enzyme';
import React from 'react';
import ForgotPasswordScreen from '../ForgotPasswordScreen';

// Note: test renderer must be required after react-native.
import renderer from 'react-test-renderer';

const createTestProps = (props: object) => ({
  navigation: {
    navigate: jest.fn() 
  },
  ...props,
});

describe('ForgotPasswordScreen', () => {
  const props = createTestProps({});
  const wrapper = shallow<ForgotPasswordScreen>(<ForgotPasswordScreen {...props} />);

  describe('rendering correctly', () => {
    it('should render a <View />', () => {
      expect(wrapper.find('View')).toHaveLength(3);
    });
  });
});

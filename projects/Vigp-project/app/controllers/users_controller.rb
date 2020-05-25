class UsersController < ApplicationController
  before_action :authenticate_user!
  before_action :find_resource, only: [:show]

  def show

  end

  private

  def find_resource
    @user = User.find(params[:id])
  end
end

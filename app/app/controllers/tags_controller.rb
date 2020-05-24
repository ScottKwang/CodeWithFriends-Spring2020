class TagsController < ApplicationController
  def all_tags
    respond_to do |format|
      format.json { render json: ActsAsTaggableOn::Tag.all, status: :ok }
    end
  end
end